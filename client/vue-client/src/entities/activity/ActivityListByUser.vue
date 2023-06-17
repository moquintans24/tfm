<template>
    <v-card >
        <v-toolbar color="#A2C0DB" flat v-if="showUserName">
            <img class="mr-3"  height="55" :src="user.image" v-if="showUserName">
            <v-toolbar-title class="v-toolbar__title" v-if="showUserName" > {{ user.name }} {{ user.lastName }}</v-toolbar-title>
        </v-toolbar>
        <!-- <v-row justify="center">
            <v-chip-group column active-class="indigo--text text--darken-4" v-model="filterActivitySelected"> 
                <v-chip class="ma-2" filter v-for="(filter,i) in getFilters" :key="i" color="#DDEEEB" link @click="filterActivitiesDoneUndone(filter,i), filterActivitySelected=filter" >
                    {{ filter }} 
                </v-chip>
            </v-chip-group>
        </v-row> -->
        <v-container v-if="alertExploreActivities" fluid>
            <v-alert dense text type="info"  elevation="2">
            {{ $t('no_activities_assigned') }}
            </v-alert>
        </v-container>
        <v-divider></v-divider>
        <v-data-table v-if="!alertExploreActivities" 
            dense
            :headers="getHeaders"
            :items="filteredInteractions"
            :items-per-page="4"
            :footer-props="{itemsPerPageText: $t('rows_per_page'),
                             'items-per-page-options': [4,8,12],
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
                
                <v-toolbar flat >
                    <v-row justify="start">
                        <v-chip-group column active-class="white--text font-weight-bold" v-model="filterActivitySelected" dark> 
                            <v-chip class="ma-2" text-color="white" filter v-for="filter in getFilters" :key="filter.id"
                            :color="filter.name == $t('activities_done') ? '#81C784' : '#EF5350'"  :value="filter.name">
                                {{ filter.name }} 
                            </v-chip>
                        </v-chip-group>
                    </v-row>
                    <v-row >
                        <v-col>
                            <v-autocomplete  class="pr-2 pl-6 pt-5"
                                v-model="selectedUnit" 
                                :items="units" clearable
                                :label="$t('search_by_unit')"
                            ></v-autocomplete>
                        </v-col>
                        <v-col>
                            <v-autocomplete class="pr-2 pt-5"
                                v-model="selectedLesson" 
                                :items="lessons" clearable
                                :label="$t('search_by_lesson')"
                            ></v-autocomplete>
                        </v-col>
                        <v-col>
                            <v-autocomplete  class="pr-5 pt-5"
                                v-model="searchValue" 
                                :items="descriptions" clearable
                                :label="$t('search_by_activity_description')"
                            ></v-autocomplete>
                        </v-col>
                    </v-row>
                </v-toolbar>
            </template>
            <template v-slot:item.activity.imagePath="{ item }">
                <img height="110px" width="170px" class="pa-1"
                        :src="item.activity.imagePath"
                    >
            </template>     
            <template v-slot:item.activity.code="{ item }">
                <v-chip :color="getColor(item)" dark >
                    {{ item.activity.code }}
                </v-chip>
            </template>   
            <template v-slot:item.actions="{ item }">
                <v-tooltip top color="#DDEEEB">
                    <template v-slot:activator="{ on, attrs }"> 
                        <v-icon  xs-small color="#ffb74d"
                            v-bind="attrs"
                            v-on="on"
                            class="mr-2"
                            @click="h5pPlay(item)"
                        >
                        mdi-play
                        </v-icon>
                    </template>
                    <span class="black--text">Play {{$t('activity')}}</span>
                </v-tooltip>
            </template>              
            
        </v-data-table>
    </v-card>
</template>


<script>
import i18n from '@/plugins/i18n';
import auth from '../../common/auth';
import store from '../../common/store';
import RepositoryFactory from '../../repositories/RepositoryFactory';
const InteractionActivityRepository = RepositoryFactory.get("interactionsActivities");
const UsersRepository = RepositoryFactory.get("users");
export default {
    name: 'ActivityListByUser',
    props:{
        classGroupId:Number,
        subscriptionId:Number
    },
    data: () => ({
      interactionsActivities:[],
      filteredInteractions:[],
      activities:[],
      user:{},
      searchValue:null,
      showUserName:false,
      descriptions:[],
      selectedLesson:null,
      selectedUnit:null,
      lessons:[],
      units:[],
      alertExploreActivities:false,
      filterActivitySelected:null,
      activitiesDoneFilterValue:null
    //   activitiesDoneUndone:[],
    
    }),
    computed:{
      getHeaders () {
			const t = this.$t.bind(this)
            return [
                      { text: t('image'), value: 'activity.imagePath', align:'start', sortable:false,class:'black--text' },
                      { text: t('code'),align: 'start', sortable: true, value: 'activity.code', class: 'black--text' },
                      { text: t('description'),align: 'start', sortable: true, value: 'activity.description', class: 'black--text ', filter:this.filterActivitiesByDescription },
                      { text: t('unit'),align: 'center', sortable: true, value: 'activity.lesson.unit.number', class: 'black--text',filter: this.filterActivitiesByUnit },
                      { text: t('lesson'),align: 'center', sortable: true, value: 'activity.lesson.number', class: 'black--text',filter: this.filterActivitiesByLesson },
                      { text: t('end_date'), align: 'start', value: 'homeworkDate', sortable: true,  class:"black--text" },
                      { text: t('duration'), align: 'start', value: 'duration', sortable: true,  class:"black--text", filter:this.filterActivitiesByDoneUndone},
                      { text: t('score'), align: 'start', value: 'score', sortable: true,  class:"black--text"},
                      { text: t('actions'), align: 'start', value: 'actions', sortable: false,  class:"black--text"},
                      
                  ]
        },
        getFilters () {
			const t = this.$t.bind(this)
            return [{name :t('activities_done'),id:0},{name:t('activities_undone'),id:1}]
        }
    },
    
    created(){
        if(!this.$route.params.subscriptionId){
            this.setData(this.classGroupId, this.subscriptionId)
        }else{
            this.setData(this.$route.params.id, this.$route.params.subscriptionId)
        }
    },
    methods:{
        async setData(classGroupId, subscriptionId){

            await InteractionActivityRepository.getByClassGroupIdAndSubscriptionId(classGroupId,subscriptionId).then(intActivities =>{
                // console.log(intActivities)
                const interactionsActivities_ = new Array()
                const activitiesDescriptions = new Array()
                const allLessons = new Array()
                const allUnits = new Array()
                for(var i=0;i<intActivities.length;i++){
                    const interactionActivity_ = {
                        activity: new Object(),
                        id: Number
                    }

                    interactionActivity_.activity.code = intActivities[i].activity.code
                    interactionActivity_.activity.description = intActivities[i].activity.description
                    interactionActivity_.activity.id = intActivities[i].activity.id
                    interactionActivity_.activity.idH5Pfile = intActivities[i].activity.idH5Pfile
                    interactionActivity_.activity.imagePath = 'http://localhost:8080/api/activities/images/'+intActivities[i].activity.imagePath
                    interactionActivity_.activity.lesson = intActivities[i].activity.lesson
                    interactionActivity_.activity.students = intActivities[i].activity.students
                    interactionActivity_.activity.title = intActivities[i].activity.title
                    interactionActivity_.activity.typeH5P = intActivities[i].activity.typeH5P
                    interactionActivity_.id = intActivities[i].id
                    interactionActivity_.duration = intActivities[i].duration
                    if(intActivities[i].homeworkDate){
                        const date_ = new Date(intActivities[i].homeworkDate)
                        interactionActivity_.homeworkDate = date_.toLocaleDateString(i18n.locale.toString()).concat(" ",date_.toLocaleTimeString(i18n.locale.toString()))
                    }else{
                        interactionActivity_.homeworkDate = intActivities[i].homeworkDate
                    }
                    activitiesDescriptions.push(intActivities[i].activity.description)
                    allLessons.push(intActivities[i].activity.lesson.number)
                    allUnits.push(intActivities[i].activity.lesson.unit.number)
                    interactionActivity_.score = intActivities[i].score
                    
                    interactionsActivities_.push(interactionActivity_)
                }
                if(interactionsActivities_.length==0){
                    this.alertExploreActivities = true
                }
                this.descriptions = [...new Set(activitiesDescriptions)].sort();
                this.lessons = [...new Set(allLessons)].sort();
                this.units = [...new Set(allUnits)].sort();
                this.interactionsActivities = interactionsActivities_
                this.filteredInteractions = interactionsActivities_
            })
            .catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });

            await UsersRepository.getBySubscriptionId(subscriptionId).then(user => {
                const user_ = new Object();
                user_.name = user.name
                user_.lastName = user.lastName
                user_.image = 'http://localhost:8080/api/users/images/'+user.linkImage
                this.user = user_

            })

            if(auth.isTeacher()) this.showUserName = true
        },
        
        getColor (item) {
            if (item.duration==null) return 'red'
            else return 'green'
        },
        filterActivitiesByDescription(value){
            if (!this.searchValue) {
                return true;
        }
            return value===this.searchValue;
        },
        filterActivitiesByUnit(value){
            if (!this.selectedUnit) {
                return true;
            }
    
            return value===this.selectedUnit;
        },
        filterActivitiesByLesson(value){
         
         // If this filter has no value we just skip the entire filter.
            if (!this.selectedLesson) {
                return true;
            }
 
            return value===this.selectedLesson;
        },
        filterActivitiesByDoneUndone(value){
            const t = this.$t.bind(this)
            // console.log("filterActivitiesByDoneUndone",this.filterActivitySelected)
            // console.log(value)
            
            if (!this.filterActivitySelected) {
                return true;
            }
            if(this.filterActivitySelected==t('activities_done')){
                if(value) return true
                
            }else{
                if(!value) return true
            }
            
        },
        h5pPlay(item){
            // console.log("show activity",item)
            this.$router.push({
                name:'H5PPlayer',
                params:{ activityId:item.activity.id,idH5Pfile: item.activity.idH5Pfile,interactionId:item.id, activity:item.activity  }
            })
        }

    }
}
</script>

<style>

.v-select__selection {
    font-size: 0.9rem !important;
} 
.v-text-field.v-text-field--solo .v-input__control{
    min-height: 10px;
}

.v-label{
  font-size: 12px;
}


tbody tr:nth-of-type(odd){
    background-color: #ECEFF1;
}

thead tr th{
    background-color: white;
    border-top-width: 1px;
    border-top-style: solid;
    border-top-color:#ECEFF1;
}

tbody tr:hover {
    background-color: #CFE7FA !important;
}

.v-toolbar__title {
  font-size: 1.0rem !important;
}
</style>