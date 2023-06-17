<template>
    <v-container fluid > 
        <v-alert v-if="showAlert"
                    border="top"
                    colored-border
                    type="info"
                    elevation="2"
                    >
                    {{ $t('no_students_realized_activity') }}
                </v-alert>
        <v-card>
            <v-data-table v-if="!showAlert"
                dense
                :headers="getHeaders"
                :items="filteredInteractionActivities"
                :items-per-page="5"
                :footer-props="{itemsPerPageText: $t('rows_per_page'),
                                            'items-per-page-all-text': $t('all')
                                            }"
                class="elevation-1">
                <template v-for="h in getHeaders" v-slot:[`header.${h.value}`]="{ header }">
                    <template v-if="h.text == $t('score')" >
                        <span >{{h.text}}
                            <v-tooltip bottom color="primary">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-icon color="primary" dark v-bind="attrs" v-on="on"> mdi-information </v-icon>
                                </template>
                                <span>{{ $t('score_out_of') }}10</span>
                            </v-tooltip>
                        </span>
                    </template>
                    <template v-else>
                        <span >{{h.text}}</span>
                    </template>
                </template>
                <template v-slot:top>
                    <v-toolbar  flat color="#A2C0DB" >
                        <img class="mr-3"  height="55"
                                :src="activity.imagePath" >
                        <v-toolbar-title class="v-toolbar__title" > {{ activity.description }}</v-toolbar-title>

                        <v-row >
                            <v-spacer></v-spacer>
                            <v-col>
                                <v-autocomplete class="pr-5 pt-5"
                                    v-model="searchValue" 
                                    :items="names" clearable
                                    :label="$t('search_by_activity_description')"
                                ></v-autocomplete>
                            </v-col>
                        </v-row>
                    </v-toolbar>
                </template>
                <template v-slot:item.subscription.userImagePath="{ item }">
                    <img height="65px" width="65px" class="pa-1"
                            :src="item.subscription.userImagePath"
                        >
                </template> 
            </v-data-table>
        </v-card>
    </v-container>
</template>

<script>
import i18n from '@/plugins/i18n';
import RepositoryFactory from '../../repositories/RepositoryFactory';
const InteractionActivity = RepositoryFactory.get("interactionsActivities");

  export default {
    name:'ActivityUserList',
    props:{
        activity:{
            type: Object,
            required:true
        }
    },
    data () {
      return {
        interactionsActivities:[],
        filteredInteractionActivities:[],
        search:'',
        activityImage:'',
        showAlert:false,
        searchValue:null,
        names:[]
      }
    },
    computed:{
        getHeaders () {
			const t = this.$t.bind(this)
            return [
                            { text: t('image'), value: 'subscription.userImagePath', align:'start', sortable:false,class:'black--text' },
                            { text: t('name'),align: 'start', sortable: true, value: 'subscription.userName', class: 'black--text', filter:this.filterInteractionsActivitiesByUserName },
                            { text: t('duration'),align: 'start', sortable: true, value: 'duration', class: 'black--text ' },
                            { text: t('score'),align: 'center', sortable: true, value: 'score', class: 'black--text' }
                            
                        ]
        }
    },
    async created(){
        // console.log("Activity",this.activity,this.activity.imagePath)
        // this.activityImage = 'http://localhost:8080/api/activities/images/'+this.activity.imagePath
        await InteractionActivity.getByActivityIdAndClassGroupId(this.activity.id, this.$route.params.id).then(intActivities => {
            // console.log("Created see students",intActivities)
                if(intActivities.length>0){
                    const interactionsActivities_ = new Array()
                    const userNames = new Array()
                    for(var i=0;i<intActivities.length;i++){
                        const interactionActivity_ = {
                            activity: new Object(),
                            id: Number,
                            subscription: new Object(),
                            teacherSubscription: new Object()
                        }
                        // this.activity = intActivities[i].activity
                        interactionActivity_.activity = intActivities[i].activity
                        interactionActivity_.id = intActivities[i].id
                        interactionActivity_.duration = intActivities[i].duration
                        const date_ = new Date(intActivities[i].homeworkDate)
                        interactionActivity_.homeworkDate = date_.toLocaleDateString(i18n.locale.toString()).concat(" ",date_.toLocaleTimeString(i18n.locale.toString()))
                        interactionActivity_.score = intActivities[i].score
                        interactionActivity_.subscription.userName = (intActivities[i].subscription.user.name).concat(" ",intActivities[i].subscription.user.lastName)
                    
                        interactionActivity_.subscription.userImagePath = 'http://localhost:8080/api/users/images/'+intActivities[i].subscription.user.linkImage

                        interactionActivity_.teacherSubscription = intActivities[i].teacherSubscription
                        
                        //Mostrar solo los alumnos que realizaron la actividad
                        if(interactionActivity_.duration){
                            interactionsActivities_.push(interactionActivity_)
                            userNames.push(interactionActivity_.subscription.userName)
                        }
                        
                    }
                    this.interactionsActivities = interactionsActivities_
                    this.filteredInteractionActivities = this.interactionsActivities
                    this.names = userNames


                }else{
                    this.showAlert = true
                }
                
            })
            .catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
    },
    methods:{
        filterInteractionsActivitiesByUserName(value){
            if (!this.searchValue) {
                return true;
            }
 
            return value===this.searchValue;
        }
    }
  }
</script>

<style>

.v-toolbar__title {
  font-size: 1.0rem !important;
}
.table_head {
      font-size: 5px !important;

 }
</style>