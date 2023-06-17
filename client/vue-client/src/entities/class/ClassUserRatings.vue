<template>
    <v-container fluid > 
        <v-btn  v-if="showActivitiesByUser" class="mx-2" color="#22555A" icon @click="showActivitiesByUser=!showActivitiesByUser" >
            <v-icon >mdi-arrow-left</v-icon>
        </v-btn>
        <v-card color="#EDFFFC" v-if="!showActivitiesByUser">
            <v-alert v-if="showAlert" border="top" colored-border type="info" elevation="2" >
                {{ $t('no_activities_to_do') }}
            </v-alert>
            <v-card-title class="v-card__title" v-if="!selectedUnit && !showAlert">{{ $t('avg_class_group_score') }}
                <span :style="classGroupAvg < 5 ? { 'color': 'red'} :  {'color': 'green'}" class="font-weight-bold ml-1"> {{ classGroupAvg }}</span>
            </v-card-title>
            <v-card-title class="v-card__title" v-if="selectedUnit && classGroupAvg && !showAlert">{{ $t('avg_class_group_score_by_unit') }} {{ selectedUnit.number }}: 
                <span :style="classGroupAvg < 5 ? { 'color': 'red'} :  {'color': 'green'}" class="font-weight-bold ml-1"> {{ classGroupAvg }}</span>
            </v-card-title>
        </v-card>
        <v-row v-if="!showActivitiesByUser">
            <v-slide-group v-model="global" class="pa-4" show-arrows mandatory active-class="indigo--text">
                <v-slide-item v-slot="{ active, toggle }" >
                    <v-btn :color="active && !selectedUnit ? 'indigo--text ' : '#DDEEEB'" class="mx-2" 
                        depressed rounded @click="setData(classGroupId), selectedUnit=null, classGroupAvg=null">
                        {{ $t('global_') }}
                    </v-btn>
                </v-slide-item> 
            </v-slide-group>
            <v-slide-group v-model="model" class="pa-4" show-arrows active-class="indigo--text">

                <v-slide-item v-for="unit,n in units" :key="unit.id" v-slot="{ active, toggle }" >
                    <v-btn :color="active ? 'indigo--text ' : '#DDEEEB'" class="mx-2"
                        depressed rounded @click="showAverageByUnit(unit.id,unit)" >
                    {{ $t('unit') }} {{ unit.number }}
                    </v-btn>
                </v-slide-item>
            </v-slide-group>
        </v-row>
        
        <v-data-table v-if="!showActivitiesByUser"
                :headers="getHeaders"
                :items="users"
                :items-per-page="5"
                :footer-props="{itemsPerPageText: $t('rows_per_page'),
                                            'items-per-page-all-text': $t('all')
                                            }"
                class="elevation-1">            
            <template v-slot:item.image="{ item }">
                <img height="80px" width="80px" class="pa-1"
                        :src="item.image">
            </template> 
            <template v-slot:item.avg_score="{ item }">
                <v-chip :color="getColor(item.avg_score)" dark>
                    {{ item.avg_score }}
                </v-chip>
            </template>
            <template v-slot:item.fullName="{ item }">
                    <span @click="clickedRouterLink(item)" style="color:#1976d2;cursor:pointer;text-decoration: underline;">{{ item.fullName }}</span>

                </template>
        </v-data-table>
        <ActivityListByUser v-if="showActivitiesByUser" :subscriptionId="itemSelected.subscriptionId" :classGroupId="classGroupId"/>
    </v-container>
  </template>

<script>
import RepositoryFactory from '../../repositories/RepositoryFactory';
const InteractionActivity = RepositoryFactory.get("interactionsActivities");
const ClassGroupsRepository = RepositoryFactory.get("classGroups");
const UsersRepository = RepositoryFactory.get("users");
const UnitsRepository = RepositoryFactory.get("units");
import ActivityListByUser from "../activity/ActivityListByUser.vue";

  export default {
    name:'ClassUserRatings',
    components:{
        ActivityListByUser
    },
    data () {
      return {
        model: null,
        global:null,
        users:[],
        classGroupAvg:null,
        units:[],
        selectedUnit:null,
        classGroupId:null,
        showAlert:false,
        showActivitiesByUser:false,
        itemSelected:null
      }
    },
    computed:{
        getHeaders () {
			const t = this.$t.bind(this)
            return [
                { text: t('image'), value: 'image', align:'start', sortable:false,class:'black--text' },
                { text: t('name'),align: 'start', sortable: true, value: 'fullName', class: 'black--text' },
                { text: t('avg_duration_in_min'),align: 'center', sortable: true, value: 'avg_duration', class: 'black--text ' },
                { text: t('avg_score'),align: 'center', sortable: true, value: 'avg_score', class: 'black--text' },
                { text: t('avg_homework_score'),align: 'center', sortable: true, value: 'avg_homework_score', class: 'black--text' },
                { text: t('avg_not_assigned_act_score'),align: 'center', sortable: true, value: 'avg_not_assigned_act_score', class: 'black--text' },
                { text: t('activities_done'),align: 'center', sortable: true, value: 'activities_done', class: 'black--text' },
                { text: t('activities_undone'),align: 'center', sortable: true, value: 'activities_undone', class: 'black--text' }  
            ]
        }
    },
    created(){
        // console.log(this.$route.params.id)
        this.setData(this.$route.params.id)
    },
    methods: {
        async setData(classGroupId){
            this.classGroupId = Number(classGroupId)
            this.model = null
          
            const classGroup = await ClassGroupsRepository.getById(classGroupId)
            this.getUnitsByBookId(classGroup.teacher.code.book.id)
       
            const usersInfo = new Array()
            var sum = 0
            await InteractionActivity.getAverageScoreStudentsByClassGroupId(classGroupId).then(async (data) => {
                if(data.length==0) this.showAlert = true
                else this.showAlert = false
                for(var i=0;i<data.length;i++){
                    // console.log("data",data)
                    const user = new Object();
                    const bdUser = await UsersRepository.getById(data[i][1])
              
                    user.fullName = bdUser.name.concat(' ',bdUser.lastName)
                    user.lastName = bdUser.lastName
                    user.image = 'http://localhost:8080/api/users/images/'+bdUser.linkImage
                    user.id = data[i][1]
                    user.subscriptionId = data[i][0]
                    if(data[i][5]) user.activities_done = data[i][5] 
                    else user.activities_done =0
                    if(data[i][4]) user.activities_undone = data[i][4]
                    else user.activities_undone=0
                    if(data[i][2]) user.avg_score = data[i][2].toFixed(2)
                    else user.avg_score ='-'
                    if(data[i][3]) user.avg_duration = data[i][3].toFixed(2)
                    else user.avg_duration='-'
                    if(data[i][7]) user.avg_not_assigned_act_score = data[i][7].toFixed(2)
                    else user.avg_not_assigned_act_score='-'
                    if(data[i][6]) user.avg_homework_score = data[i][6].toFixed(2)
                    else user.avg_homework_score='-'
  
                    usersInfo.push(user);
                    sum +=  data[i][2]
                }
                this.classGroupAvg = (sum/data.length).toFixed(2);
           
                this.users = usersInfo.sort( (a, b) => {
                                if (a.lastName.toLowerCase() < b.lastName.toLowerCase()) {
                                    return -1;
                                }
                                if (a.lastName.toLowerCase() > b.lastName.toLowerCase()) {
                                    return 1;
                                }
                                return 0;
                                });
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        async getUnitsByBookId(bookId){
            await UnitsRepository.getByBookId(bookId).then((units) => {
                // console.log("units", units)
                this.units = units
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        async showAverageByUnit(n,unit) {
            const index = this.units.indexOf(unit)
            this.model = index;
            this.global = null;
            
            this.selectedUnit = unit
            const usersInfo = new Array()
            var sum = 0
            await InteractionActivity.getAverageScoreStudentsByClassGroupIdAndUnitId(this.$route.params.id,unit.id).then(async (data) => {
                if(data.length==0) this.showAlert = true
                else this.showAlert = false
                for(var i=0;i<data.length;i++){
                    
                    const user = new Object();
                    const bdUser = await UsersRepository.getById(data[i][1])
                    // console.log("user",bdUser)
                    // console.log("data",data[i][3])
                    user.fullName = bdUser.name.concat(' ',bdUser.lastName)
                    user.lastName = bdUser.lastName
                    user.image = 'http://localhost:8080/api/users/images/'+bdUser.linkImage
                    user.id = data[i][1]
                    user.subscriptionId = data[i][0]
                    if(data[i][6] || data[i][6]==0) user.activities_done = data[i][6] 
                    else user.activities_done =0
                    if(data[i][5] || data[i][5]==0) user.activities_undone = data[i][5]
                    else user.activities_undone=0
                    if(data[i][3] || data[i][3]==0) user.avg_score = data[i][3].toFixed(2)
                    else user.avg_score ='-'
                    if(data[i][4] || data[i][4]==0) user.avg_duration = data[i][4].toFixed(2)
                    else user.avg_duration='-'
                    if(data[i][8] || data[i][8]==0) user.avg_not_assigned_act_score = data[i][8].toFixed(2)
                    else user.avg_not_assigned_act_score='-'
                    if(data[i][7] || data[i][7]==0) user.avg_homework_score = data[i][7].toFixed(2)
                    else user.avg_homework_score='-'
                    // console.log("user_info",user)
                    usersInfo.push(user);
                    sum +=  data[i][3]
                }

                if(data.length>0) this.classGroupAvg = (sum/data.length).toFixed(2);
                else this.classGroupAvg = null
                
                this.users = usersInfo.sort( (a, b) => {
                                if (a.lastName.toLowerCase() < b.lastName.toLowerCase()) {
                                    return -1;
                                }
                                if (a.lastName.toLowerCase() > b.lastName.toLowerCase()) {
                                    return 1;
                                }
                                return 0;
                                });
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        getColor (avg_score) {
            if (avg_score < 5) return 'red'
            else if (avg_score >= 5) return 'success'
        },
        clickedRouterLink(item){
            // console.log("item",item)
            this.showActivitiesByUser = !this.showActivitiesByUser
            this.itemSelected = item
        },
    },
  }
</script>

<style scoped>
.v-card__title {
    color:#22555A;
    justify-content: center;
}
</style>