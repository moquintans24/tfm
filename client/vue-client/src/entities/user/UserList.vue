<template>
    <v-layout align-center justify-center>
        <v-flex xs12 sm12 md10>
            <v-container fluid >
                <v-card >
                    <v-card-title class="v-card__title_" 
                    style="background: #DDEEEB; background: linear-gradient(to right, #DDEEEB 0%,#CFE7FA 50%, #22555A 100%);" height="92px"> {{ $t('users') }} </v-card-title>
                    <v-data-table
                        :headers="getHeaders"
                        :items="filteredUsers"
                        :items-per-page="5"
                        :search="search"
                        :footer-props="{itemsPerPageText: $t('rows_per_page'),
                                        'items-per-page-all-text': $t('all')
                                        }"
                                        
                        class="elevation-1">
                        <template v-slot:item.image="{ item }">
                            <img height="115px" class="pa-1"
                                    :src="item.image"
                                >
                        </template>
                        <template v-slot:top>
                            <v-toolbar flat>
                                <v-row >
                                    <v-col>
                                        <v-select 
                                            v-model="selectedUserAuthority.name"
                                            :items="usersAuth"
                                            class="v-text__field pr-5 pt-5"
                                            :label="$t('authority')"
                                            v-on:change="filterUsersList()"
                                            single-line
                                            hide-details
                                            ></v-select>
                                        
                                    </v-col>
                                    <v-col>
                                        <v-autocomplete class="pr-5 pt-5"
                                            v-model="searchValue" 
                                            :items="names" clearable
                                            :label="$t('search_by_user_fullname')"
                                        ></v-autocomplete>
                                    </v-col>
                                </v-row>
                                
                                <v-btn class="mx-2" color="#22555A" fab small dark outlined >
                                    <v-icon @click="dialog = true">mdi-plus</v-icon>
                                </v-btn>
                                <NewUser :dialog="dialog" :isHomePage="false" @close-dialog="dialog =false"/>
                            </v-toolbar>
                        </template>
                        <template v-slot:item.fullName="{ item }">
                            <router-link :to="{ name: 'UserDetail', params: { id: item.id } }">
                            {{ item.fullName }}
                        </router-link>
                        </template>
                        
                        <template v-slot:item.actions="{ item }">
                            <v-tooltip top color="#DDEEEB"  >
                                <template v-slot:activator="{ on, attrs }"> 
                                    <v-icon color="red"
                                        v-bind="attrs"
                                        v-on="on"
                                        class="mr-2"
                                        @click="unsubscribeUser(item)"
                                    >
                                        mdi-account-remove
                                    </v-icon>
                                </template>
                                <span class="red--text">{{$t('unsubscribe')}}</span>
                            </v-tooltip>
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
                    <v-container>
                        <v-card-text class="v-card__text_dialog">{{$t('unsubscribe_user_question')}} {{unsubscribedItem.fullName}}? </v-card-text>
                    </v-container>
                    <v-card-actions>
                        <v-btn color="red" text @click="closeDelete">{{ $t('cancel') }}</v-btn>
                        <v-spacer></v-spacer>
                        <v-btn color="green darken-1" text @click="deleteItemConfirm">{{ $t('ok') }}</v-btn>
                    </v-card-actions>
                </v-card>
                </v-dialog>
          </v-container>
      </v-flex>
    </v-layout>
</template>


<script>
import auth from "../../common/auth";
import RepositoryFactory from "@/repositories/RepositoryFactory";
const UsersRepository = RepositoryFactory.get("users");
import NewUser from './NewUser';
import i18n from '@/plugins/i18n';

export default {
    name: 'UserList',
    components:{
        NewUser
    },
    data: () => ({
        users: [],
        usersAuth:[],
        filteredUsersByAuthority:[],
        selectedUserAuthority: {
            name: '',
            originName:''
        }, 
        usersAuthOriginName:[],
        valid:true,
        user:{},
        search:'',
        dialog:false,  
        dialogDelete: false,
        itemIndex: -1,
        unsubscribedItem: {
            email: '',
            fullName: '',
            id: '',
            login: '',
            registerDate: '',
        },
        defaultItem: {
            email: '',
            fullName: '',
            id: '',
            login: '',
            registerDate: '',
        },
        filteredUsers:[],
        names:[],
        searchValue:null
    }),
    async beforeRouteEnter(to, from, next) {
        const users = await UsersRepository.getAll();
        next(vm => vm.setData(users));
    },
    computed: {
        isAdmin() {
            return auth.isAdmin();
        },
        form () {
            return {
                className: this.className
            }
        },
        getHeaders () {
			const t = this.$t.bind(this)
                return [
                            { text: t('image'), value: 'image', align:'start', sortable:false,class:'black--text' },
                            { text: t('name'),align: 'start', sortable: true, value: 'fullName', class: 'black--text',filter:this.filterByName },
                            { text: 'Email', align: 'start', sortable: true, value: 'email', class: 'black--text' },
                            { text: 'Login', align: 'start', sortable: true, value: 'login', class: 'black--text' },
                            { text: t('register_date'), align: 'start', value: 'registerDate', sortable: true, class:"black--text"},
                            { text: t('actions'), value: 'actions', align: 'center', sortable: false, class:"black--text justify-center"}
                            
                        ]
        }
    },
    created(){
        this.dialog = false
    },
    methods: {
        validate () {
            this.$refs.form.validate()
        },
        reset () {
            this.$refs.form.reset()
        },
        resetValidation () {
            this.$refs.form.resetValidation()
        },
        required_field(msg){
            return [value => !!value || msg]
        },
        required_email(msg){
            return [ v => !!v || msg,
                     v => /.+@.+\..+/.test(v) || 'Email debe ser válido']
        },
        setData(data) {
            const options = { year: 'numeric', month: '2-digit', day: '2-digit' }
            const t = this.$t.bind(this)
            const users_ = new Array()
            const authorities = new Array()
            for(var i = 0; i < data.length; i++){
                const usr = new Object()
                usr.id = data[i].id
                usr.fullName = data[i].name.concat(' ',data[i].lastName)
                usr.email = data[i].email
                usr.login = data[i].login
                usr.image = this.getRouteImage(data[i].linkImage)
                usr.authority = data[i].authority
               
                const date_ = new Date(data[i].registerDate)
                
                usr.registerDate = date_.toLocaleDateString(i18n.locale.toString())
                users_.push(usr)
              
                const userAuthority = new Object()
                switch (data[i].authority) {
                    case 'EDITOR':
                        authorities.push(t('editors'))
                        userAuthority.name = t('editors')
                        userAuthority.originName = data[i].authority
                        this.selectedUserAuthority.name = t('editors')
                        this.selectedUserAuthority.originName = data[i].authority
                        this.usersAuthOriginName.push(userAuthority)     
                        break;
                    case 'TEACHER':
                        authorities.push(t('teachers'))
                        userAuthority.name = t('teachers')
                        userAuthority.originName = data[i].authority
                        this.usersAuthOriginName.push(userAuthority)
                        
                        break;
                    case 'USER':
                        authorities.push(t('students'))
                        userAuthority.name = t('students')
                        userAuthority.originName = data[i].authority
                        this.usersAuthOriginName.push(userAuthority)
                        
                        break;
                    default:
                        break;
                }
            }
            this.usersAuth = [... new Set(authorities)]
            this.usersAuthOriginName = [...new Map(this.usersAuthOriginName.map((item) => [item["name"], item])).values()]
            this.users = users_.sort((a,b) => new Date(b.registerDate).getTime() - new Date(a.registerDate).getTime());
            this.users = users_
            
            this.filterUsersList()
        },
        filterUsersList(){
            const result = this.usersAuthOriginName.find(o => o.name === this.selectedUserAuthority.name)
            this.selectedUserAuthority.originName = result.originName
           
            const users_ = this.users.filter(user => 
                user.authority == this.selectedUserAuthority.originName
            )
            
            const names_ = new Array()
            for(var i=0;i<users_.length;i++){
                names_.push(users_[i].fullName)
            }
            this.searchValue = null
            this.names = names_.sort()
            this.filteredUsersByAuthority = users_ 
            this.filteredUsers = this.filteredUsersByAuthority
                
             
        },
        getRouteImage(linkImage){
            return 'http://localhost:8080/api/users/images/'+linkImage
        },
        unsubscribeUser(item){
            this.itemIndex = this.users.indexOf(item)
            this.unsubscribedItem = item
            this.dialogDelete = true
        },
        async deleteItemConfirm () {
            
            await UsersRepository.deleteById(this.unsubscribedItem.id)
            .then( () => {
                const find = this.filteredUsers.find(u => u.id == this.unsubscribedItem.id)
                const index = this.filteredUsers.indexOf(find)
                this.filteredUsers.splice(index,1)
                this.$notify({ 
                    text: 'Usuario dado de baja',
                    type: "success"
                })
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
                this.unsubscribedItem = Object.assign({}, this.defaultItem)
                this.itemIndex = -1
            })
        },
        filterByName(value){
            if(!this.searchValue){
                return true
            }
            return value === this.searchValue
        },
        
    }
};
</script>
    
<style scoped>
.v-card__title_ {
color:#22555A;
font-weight: bold;
justify-content: center;
/* font-family:  cursive; */
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

.v-text__field{
    min-width:20px;
    max-width:200px;
}

.v-select_{
    min-width:20px;
    max-width:160px;
}
</style>
    
  