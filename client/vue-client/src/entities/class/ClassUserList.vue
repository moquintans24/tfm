<template>
    <v-container fluid >
        <v-card>
            <v-data-table
                dense
                :headers="getHeaders"
                :items="filteredUsers"
                :items-per-page="5"
                :footer-props="{'itemsPerPageText': $t('rows_per_page'),
                                            'items-per-page-all-text': $t('all')
                                            }"
                class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat >           
                        <v-spacer></v-spacer>
                        <v-autocomplete class="pr-5 pt-5"
                            v-model="searchValue" 
                            :items="names" clearable
                            :label="$t('search_by_user_fullname')"
                        ></v-autocomplete>   
                    </v-toolbar>
                </template>
                <template v-slot:item.image="{ item }">
                    <img height="80px" width="80px" class="pa-1"
                            :src="item.image"
                        >
                </template> 
                <template v-slot:item.actions="{ item }">
                    <v-tooltip top color="#DDEEEB"  v-if="item.state != $t('subscription_pending_state')">
                        <template v-slot:activator="{ on, attrs }"> 
                            <v-icon 
                                v-bind="attrs"
                                v-on="on" color="red darken-2"
                                class="mr-2"
                                @click="deleteUser(item)"
                            >
                                mdi-account-remove
                            </v-icon>
                        </template>
                        <span class="red--text">{{$t('delete')}}</span>
                    </v-tooltip>
                    <v-row v-else justify="center">
                        <v-tooltip top color="#DDEEEB"  >
                            <template v-slot:activator="{ on, attrs }"> 
                                <v-icon
                                    v-bind="attrs"
                                    v-on="on"
                                    class="mr-2" color="red darken-2" @click="cancelSubscription(item)"
                                >
                                    mdi-close
                                </v-icon>
                            </template>
                            <span class="red--text font-weight-bold">{{$t('cancel')}}</span>
                        </v-tooltip>
                        <v-tooltip top color="#DDEEEB">
                            <template v-slot:activator="{ on, attrs }"> 
                                <v-icon
                                    v-bind="attrs"
                                    v-on="on"
                                    class="mr-2" color="green darken-2" @click="acceptSubscription(item)"
                                >
                                    mdi-check-bold
                                </v-icon>
                            </template>
                            <span class="green--text font-weight-bold">{{$t('ok')}}</span>
                        </v-tooltip>
                        
                    </v-row>
                    
                </template>
                <template v-slot:item.fullName="{ item }">
                    <router-link v-if="item.state == $t('subscription_completed_state')" :to="{ name: 'UserProfile', params: { id: item.id }}">
                        {{ item.fullName }}
                    </router-link>
                    <span v-else >{{ item.fullName }}</span>
                </template>
                <template v-slot:item.state="{ item }">
                    <span v-if="item.stateSubscriptionStyle == 'red'" style="color:red;">{{ item.state }}</span>
                    <span v-else-if="item.stateSubscriptionStyle == 'green'" style="color:green;">{{ item.state }}</span>
                    <span v-else style="color:orange;">{{ item.state }}</span>
                </template>

            </v-data-table>
        </v-card>
        <v-dialog v-model="dialogDelete" persistent max-width="600px">
            <v-card>
                <v-card-title class="v-card__title_dialog" >{{$t('unsubscribe_user')}} 
                    <v-spacer></v-spacer>
                    <v-btn icon  @click="closeDelete">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-card-title>
                <v-card-text class="v-card__text_dialog">{{$t('unsubscribe_user_question')}} {{deletedItem.fullName}}? </v-card-text>
                <v-card-actions>
                    <v-btn color="red" text @click="closeDelete">{{ $t('cancel') }}</v-btn>
                    <v-spacer></v-spacer>
                    <v-btn color="green darken-1" text @click="deleteItemConfirm">{{ $t('ok') }}</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-container>
</template>


<script>
import i18n from '@/plugins/i18n';
import RepositoryFactory from '../../repositories/RepositoryFactory';
const ClassGroupsRepository = RepositoryFactory.get("classGroups");
const UsersRepository = RepositoryFactory.get("users");
const SubscriptionsRepository = RepositoryFactory.get("subscriptions");

  export default {
    data () {
      return {
        search:'',
        students:[],
        dialogDelete:false,
        deletedItem: {},
        defaultItem: {} ,
        itemIndex: -1,
        itemSelected:{},
        names:[],
        searchValue:null,
        filteredUsers:[]
      }
    },
    computed:{
        getHeaders () {
			const t = this.$t.bind(this)
            return [
                            { text: t('image'), value: 'image', align:'start', sortable:false,class:'black--text' },
                            { text: t('name'),align: 'start', sortable: true, value: 'fullName', class: 'black--text', filter:this.filterByUserName },
                            { text: t('state'),align: 'start', sortable: true, value: 'state', class: 'black--text ' },
                            { text: t('actions'),align: 'center', sortable: true,  value:'actions',class: 'black--text' }
                            
                        ]
        },
    },
    async created(){
        // await ClassGroupsRepository.getById(this.$route.params.id)
        // console.log(this.$route.params.id)
        const t = this.$t.bind(this)
        const students_ = new Array()
        const studentsName = new Array()
        await SubscriptionsRepository.getByClassGroupId(this.$route.params.id).then(subscriptions => {
            // console.log("subscriptions",subscriptions)
            for(var i=0;i<subscriptions.length;i++){
                const student_ = new Object()
                student_.subscriptionId = parseInt(subscriptions[i].id)
                student_.id = subscriptions[i].user.id
                student_.fullName = (subscriptions[i].user.name).concat(" ",subscriptions[i].user.lastName)
                student_.image = 'http://localhost:8080/api/users/images/'+subscriptions[i].user.linkImage
                student_.classGroupId = parseInt(this.$route.params.id )
                // student_.state = subscriptions[i].state
                switch (subscriptions[i].state) {
                    case 'COMPLETED':
                        student_.state = t('subscription_completed_state')
                        student_.stateSubscriptionStyle = 'green'
                        break;
                    case 'CANCELLED':
                        student_.state = t('subscription_cancelled_state')
                        student_.stateSubscriptionStyle = 'red'
                        break;
                    case 'PENDING':
                        student_.state = t('subscription_pending_state')
                        student_.stateSubscriptionStyle = 'orange'
                        break;
                    default:
                        break;
                }
                students_.push(student_)
                studentsName.push(student_.fullName)
            }
            this.students = students_.sort((a,b) => a.state > b.state).reverse();
            this.filteredUsers = this.students
            this.names = studentsName.sort()
        })
    },
    methods:{
        deleteUser(item){
            this.itemIndex = this.students.indexOf(item)
            this.deletedItem = Object.assign({}, item)
            this.dialogDelete = true
        },
        async deleteItemConfirm () {
            const t = this.$t.bind(this)
            await SubscriptionsRepository.deleteClassGroupId(this.deletedItem.subscriptionId)
            .then( () => {
                this.$notify({ 
                    text: t('user_deleted_class_group'),
                    type: "success"
                })
                this.students.splice(this.itemIndex, 1)
            })
            .catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type:"error"
                })
            }) 
            this.closeDelete()
        },
        closeDelete () {
            this.dialogDelete = false
            this.$nextTick(() => {
                this.deletedItem = Object.assign({}, this.defaultItem)
                this.itemIndex = -1
            })
        },
        async cancelSubscription(item){
            const t = this.$t.bind(this)
            
            await SubscriptionsRepository.cancelSubscriptionToClassGroup(item.subscriptionId).then(()=>{
                const itemIndex = this.students.indexOf(item)
                this.students.splice(itemIndex, 1)
  
            });
            this.$root.$refs.ClassDetail.pendingSubscriptions_();
        },
        async acceptSubscription(item){
            const t = this.$t.bind(this)
            await SubscriptionsRepository.acceptSubscriptionToClassGroup(item.subscriptionId).then(()=>{
                item.state = t('subscription_completed_state')
                item.stateSubscriptionStyle = 'green'
            });
            this.$root.$refs.ClassDetail.pendingSubscriptions_();
        },
        filterByUserName(value){
            if(!this.searchValue){
                return true;
            }
            return value===this.searchValue;
         
        }
    }
  }
</script>

<style scoped>

.v-toolbar__title {
  font-size: 1.0rem !important;
}

.v-card__title_dialog {
background-color:#DDEEEB;
font-weight: bold;
justify-content: start;
}

.v-card__text_dialog {
    font-weight: bold;
    text-align: start;
}
</style>