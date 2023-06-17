<template>
    <v-layout align-center justify-center>
        <v-flex xs12 sm12 md12>
            <v-container fluid >
                <v-btn v-if="showActivityUserList" class="mx-2" color="#22555A" icon @click="showActivityUserList=false" >
                    <v-icon >mdi-arrow-left</v-icon>
                </v-btn>
                <ActivityUserList v-if="showActivityUserList" :activity="activitySelected" :activityId="activitySelected.id"/>  
                <v-card v-if="!showActivityUserList">
                    <v-data-table
                        dense
                        :headers="getHeaders"
                        :items="filteredActivities"
                        :search="search"
                        :items-per-page="4"
                        :footer-props="{itemsPerPageText: $t('rows_per_page'),
                                        'items-per-page-options': [4,8,12],
                                                    'items-per-page-all-text': $t('all')
                                                    }"
                        class="elevation-1" :loading="loading" :loading-text="$t('loading_activities')" >
                        <template v-slot:top>
                            <v-toolbar flat  >
                                <v-row >
                                    <v-col>
                                        <v-autocomplete class="pr-5 pt-5"
                                            v-model="selectedUnit" 
                                            :items="units" clearable
                                            :label="$t('search_by_unit')"
                                        ></v-autocomplete>
                                    </v-col>
                                    <v-col>
                                        <v-autocomplete class="pr-5 pt-5"
                                            v-model="selectedLesson" 
                                            :items="lessons" clearable
                                            :label="$t('search_by_lesson')"
                                        ></v-autocomplete>
                                    </v-col>
                                    <v-col>
                                        <v-autocomplete class="pr-5 pt-5"
                                            v-model="searchValue" 
                                            :items="descriptions" clearable
                                            :label="$t('search_by_activity_description')"
                                        ></v-autocomplete>
                                    </v-col>
                                </v-row>
                                <v-dialog persistent  v-model="dialogAssignHomewordDate"  max-width="500" >
                                    <v-card>
                                        <v-card-title class="v-card__title_dialog" v-if="editEndDate" > {{ $t('edit_end_date') }}
                                            <v-spacer></v-spacer>
                                            <v-btn icon  @click="editEndDate = false,homeworkDate={},dialogAssignHomewordDate=false">
                                                <v-icon>mdi-close</v-icon>
                                            </v-btn>
                                        </v-card-title>
                                        <v-card-title class="v-card__title_dialog" v-if="assignHomeworkDate"> {{ $t('assign_activity') }}
                                            <v-spacer></v-spacer>
                                            <v-btn icon  @click="assignHomeworkDate = false,homeworkDate={},dialogAssignHomewordDate=false">
                                                <v-icon>mdi-close</v-icon>
                                            </v-btn>
                                        </v-card-title>
                                        <v-spacer></v-spacer>
                                        <v-card-text>
                                            <v-row class="mt-10">
                                                <v-col>
                                                    <v-form ref="form">
                                                        <v-text-field outlined
                                                            v-model="homeworkDate.date"
                                                            :label="$t('end_date')"
                                                            :rules="validate_date('date')"
                                                            type="date"
                                                            required
                                                        ></v-text-field>
                                                        <v-text-field outlined
                                                            v-model="homeworkDate.time"
                                                            :label="$t('hour')"
                                                            :rules="validate_date('hour')"
                                                            type="time"
                                                            required
                                                        ></v-text-field>
                                                    </v-form>
                                                </v-col>
                                            </v-row>
                                        </v-card-text>
                                        <v-card-actions>
                                        <v-spacer></v-spacer>
                                        <v-btn v-if="editEndDate" color="green darken-1" :disabled="!isFormValid" text @click="editEndDate = false, dialogAssignHomewordDate = false,
                                        updateEndDate()" >
                                            {{ $t('ok') }}
                                        </v-btn>
                                        <v-btn v-if="assignHomeworkDate" color="green darken-1" :disabled="!isFormValid"
                                            text
                                            @click="assignHomeworkDate = false, dialogAssignHomewordDate = false, assignActivity(activityToEdit.id)"
                                        >{{ $t('ok') }}
                                        </v-btn>
                                    </v-card-actions>
                                    </v-card>
                                </v-dialog>
                                
                            </v-toolbar>

                        </template>
                        <template v-slot:item.imagePath="{ item }" > 
                            <img height="110px" width="170px" class="pa-1"
                                    :src="item.imagePath"
                                >
                        </template>   
                        <template v-slot:item.code="{ item }">
                            <v-tooltip top color="#DDEEEB">
                                <template v-slot:activator="{ on }">
                                    <v-chip v-on="on"
                                        :color="getColor(item)"
                                        outlined
                                        >
                                    {{ item.code }}
                                    </v-chip>
                                
                                </template>
                                <span class="black--text" v-if="!item.lessonAvailable">{{ $t('disabled')}}</span>
                                <span class="black--text" v-else>{{ $t('enabled')}}</span>
                            </v-tooltip>
                        </template>
                        <template v-slot:item.actions="{ item }">

                            <v-tooltip top color="#DDEEEB"  v-if="!item.homeworkDate">
                                <template v-slot:activator="{ on, attrs }"> 
                                    <v-icon  xs-small :disabled="!item.lessonAvailable" color="#22555A"
                                        v-bind="attrs"
                                        v-on="on"
                                        class="mr-2"
                                        @click="activityToEdit=item, assignHomeworkDate=true, dialogAssignHomewordDate=true"
                                    >
                                    mdi-calendar
                                    </v-icon>
                                </template>
                                <span class="black--text">{{$t('assign_activity')}}</span>
                            </v-tooltip>
                            
                            <v-tooltip top color="#DDEEEB"  v-if="item.homeworkDate">
                                <template v-slot:activator="{ on, attrs }"> 
                                    <v-icon  xs-small color="#22555A"
                                        v-bind="attrs"
                                        v-on="on"
                                        class="mr-2"
                                        @click="_editEndDate(item), editEndDate = true, dialogAssignHomewordDate = true"
                                    >
                                    mdi-pencil
                                    </v-icon>
                                </template>
                                <span class="black--text">{{$t('edit_end_date')}}</span>
                            </v-tooltip>
                            

                            <v-tooltip top color="#DDEEEB"  v-if="item.assigned">
                                <template v-slot:activator="{ on, attrs }"> 
                                    <v-icon xs-small color="#009688"
                                        v-bind="attrs"
                                        v-on="on"
                                        class="mr-2"
                                        @click="showActivityUserList = true,seeStudents(item)"
                                    >
                                    mdi-account-multiple
                                    </v-icon>
                                </template>
                                <span class="black--text">{{$t('see_activity_students')}}</span>
                            </v-tooltip>

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
            </v-container>
        </v-flex>
    </v-layout>
</template>

<script>
import i18n from '@/plugins/i18n';
import ActivityUserList from './ActivityUserList.vue';
import RepositoryFactory from '../../repositories/RepositoryFactory';
import store from '../../common/store';
const InteractionActivity = RepositoryFactory.get("interactionsActivities");
const ActivityRepository = RepositoryFactory.get("activities");
const ClassGroupsRepository = RepositoryFactory.get("classGroups");
const SubscriptionRepository = RepositoryFactory.get("subscriptions");

  export default {
    name: 'ActivityList',
    props:{
        teacherSubscription:{
            type:Object,
            required: true
        }
    },
    components:{
        ActivityUserList
    },
    data: () => ({
        interactionsActivities:[],
        filteredActivities:[],
        activities:[],
        search:'',
        showActivityUserList: false,
        activitySelected:'',
        homeworkDate:{},
        required_field_:false,
        isFormValid:true,
        untilLesson:{}, //lección hasta la que hay contenido visible
        editEndDate:false,
        assignHomeworkDate:false,
        dialogAssignHomewordDate:false,
        activityToEdit:{},
        selectedUnit:null,
        selectedLesson:null,
        searchValue:null,
        descriptions:[],
        lessons:[],
        units:[],
        loading:true,
        classGroup:null
    }),
    computed:{
        getHeaders () {
			const t = this.$t.bind(this)
            return [
                            { text: t('image'), value: 'imagePath', align:'start', sortable:false,class:'black--text' },
                            { text: t('code'),align: 'start', sortable: true, value: 'code', class: 'black--text' },
                            { text: t('description'),align: 'start', sortable: true, value: 'description', class: 'black--text ', filter:this.filterActivitiesByDescription  },
                            { text: t('unit'),align: 'center', sortable: true, value: 'lesson.unit.number', class: 'black--text',filter: this.filterActivitiesByUnit },
                            { text: t('lesson'),align: 'center', sortable: true, value: 'lesson.number', class: 'black--text',filter: this.filterActivitiesByLesson },
                            { text: t('end_date'), align: 'start', value: 'homeworkDate', sortable: true,  class:"black--text"},
                            { text: t('students'),  align: 'center', value:'students',sortable: true, class:"black--text justify-center"},
                            { text: t('actions'), value: 'actions', align: 'center', sortable: false, class:"black--text justify-center"}
                            
                        ]
        }
    },
    created(){
        if(this.teacherSubscription){
            this.setData(this.teacherSubscription)
        }
        
    },
    methods:{
        async setData(teacherSubs){
            const interactionsActivities_ = new Array()
            const activities_ = new Array()
            
            await InteractionActivity.getByClassGroupId(this.$route.params.id).then(intActivities => {
                for(var i=0;i<intActivities.length;i++){
                    const interactionActivity_ = {
                        activity: new Object(),
                        id: Number,
                        subscription: new Object(),
                        teacherSubscription: new Object()
                    }
                    const activity_ = new Object()

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
                    
                    interactionActivity_.score = intActivities[i].score
                    interactionActivity_.subscription = intActivities[i].subscription
                    interactionActivity_.teacherSubscription = intActivities[i].teacherSubscription
                    
                    interactionsActivities_.push(interactionActivity_)
                }
                this.interactionsActivities = interactionsActivities_
            })
            .catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
            let classGroup_ = {};
            //Total de estudiantes para cada actividad realizada
            const studentsByActivity = await ActivityRepository.getTotalStudentsRealizedActivity(this.$route.params.id)
            await ClassGroupsRepository.getById(this.$route.params.id).then(classGroup => {
                if(classGroup.lesson){
                    this.untilLesson = classGroup.lesson
                }
                classGroup_ = classGroup
                this.classGroup = classGroup
                
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
            
        
            await ActivityRepository.getByBookId(teacherSubs.code.book.id).then(async activities => {
                const activitiesDescriptions = new Array()
                const allLessons = new Array()
                const allUnits = new Array()
                if(activities){
                    for(var i=0;i<activities.length;i++){
                        const activity_ = new Object()
                        
                        activity_.code = activities[i].code
                        activity_.description = activities[i].description
                        activity_.id = activities[i].id
                        activity_.idH5Pfile = activities[i].idH5Pfile
                        activity_.lesson = activities[i].lesson
                        activity_.imagePath = 'http://localhost:8080/api/activities/images/'+activities[i].imagePath
                        activity_.title = activities[i].title
                        activity_.typeH5P = activities[i].typeH5P
                        const found = this.interactionsActivities.find(element => element.activity.id == activities[i].id)
                        if(found != null) activity_.homeworkDate = found.homeworkDate
                        const totalStudents = studentsByActivity.find(element => element[0] == activities[i].id)
                        if(totalStudents!=null) activity_.students = totalStudents[1]
                        // console.log("totalStudents",totalStudents)
                        const hasInteractions = await this.getActivitiesWithInteractions(activities[i].id) //Comprobamos si la actividad tiene alguna interación de actividad
                        //comprobamos que esa actividad esté en una lección menor o igual que la leccion que tenemos en el grupo de clase (actividades visibles)
                        //buscamos todas las actividades de las lecciones visibles en el grupo de clase
                        activity_.assigned = false
                        activity_.lessonAvailable = false
                        if(classGroup_.lesson){
                            const availableActivities = await ActivityRepository.getAvailablesByClassGroupId(this.$route.params.id)
                            //console.log(availableActivities)
                            if(hasInteractions && availableActivities.find(a => a.id === activities[i].id)!=null) activity_.assigned = true
                            else activity_.assigned = false

                            if(activities[i].lesson.id <= classGroup_.lesson.id) activity_.lessonAvailable = true
                            
                        }

                        activitiesDescriptions.push(activities[i].description)
                        allLessons.push(activities[i].lesson.number)
                        allUnits.push(activities[i].lesson.unit.number)

                        activities_.push(activity_)
                        
                    }
                    const ids = activities_.map(a => a.id)
                    const filtered = activities_.filter((id, index) => !ids.includes(id, index + 1))
                    this.activities =  filtered
                    this.filteredActivities = this.activities
                    this.loading = false
                    this.descriptions = [...new Set(activitiesDescriptions)].sort();
                    this.lessons = [...new Set(allLessons)].sort();
                    this.units = [...new Set(allUnits)].sort();
                }
                

                
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
   
        },

        seeStudents(activity){
            this.activitySelected = activity
        },
        async getActivitiesWithInteractions(activityId){
            return await ActivityRepository.getByIdWithInteractions(activityId)
        },
        async assignActivity(activityId){
            
            if((!this.homeworkDate.date && this.homeworkDate.time) || (this.homeworkDate.time && !this.homeworkDate.date) ){
                this.validate_date()
            }else {
                if(this.homeworkDate.date){
                    await InteractionActivity.assignInteractionByActivityIdAndClassGroupId(activityId, this.$route.params.id,this.homeworkDate.date.concat('T',this.homeworkDate.time))
                    .then(() => this.setData(this.teacherSubscription))
                    this.homeworkDate = {}
                }else{
                    await InteractionActivity.assignInteractionByActivityIdAndClassGroupId(activityId, this.$route.params.id)
                    .then(() => this.setData(this.teacherSubscription))
                    this.homeworkDate = {}
                }
                
            }
        },
        validate_date(field){
            const t = this.$t.bind(this)
            if((this.homeworkDate.date && !this.homeworkDate.time)){
                this.isFormValid = false
                // console.log("required_hour")
                return [value => !!value || t('required_hour')]
        
            }else if((this.homeworkDate.time && !this.homeworkDate.date) ){
                this.isFormValid = false
                // console.log("required_date")
                return [value => !!value || t('required_date')]
            }else this.isFormValid = true
            
           
        },
        getColor (item) {
            if(!item.lessonAvailable) return 'grey'
            else return 'black'
        },
        _editEndDate(activity){
            this.activityToEdit = activity
            if(activity.homeworkDate){
                const date = activity.homeworkDate.split(' ')[0]
                const time = activity.homeworkDate.split(' ')[1]
                this.homeworkDate.date = date.split('/')[2]+'-'+date.split('/')[1]+'-'+date.split('/')[0]
                this.homeworkDate.time = time
            }
            
        },
        async updateEndDate(){
            // console.log(this.homeworkDate)
            const index = this.activities.find(a => a.id === this.activityToEdit.id)
     
            if((this.homeworkDate.date && !this.homeworkDate.time) || (this.homeworkDate.date && this.homeworkDate.time == '')
            || (this.homeworkDate.time && !this.homeworkDate.date) || (this.homeworkDate.time && this.homeworkDate.date =='')){
                this.validate_date()
                this.homeworkDate = {}
            }else {
                if(this.homeworkDate.date && this.homeworkDate.time){
                    await InteractionActivity.updateHomeworkDateByClassGroupIdTeacherSubscriptionIdAndActivityId(this.$route.params.id, this.teacherSubscription.id,this.activityToEdit.id,
                    this.homeworkDate.date.concat('T',this.homeworkDate.time))
                    .then(() => this.setData(this.teacherSubscription))
                    this.homeworkDate = {}
                }else{
                    await InteractionActivity.updateHomeworkDateByClassGroupIdTeacherSubscriptionIdAndActivityId(this.$route.params.id, this.teacherSubscription.id,this.activityToEdit.id)
                    .then(() => this.setData(this.teacherSubscription))
                    this.homeworkDate = {}  
                }
                
            }
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
            if (!this.selectedLesson) {
                return true;
            }
 
            return value===this.selectedLesson;
        },
        h5pPlay(item){
            // console.log("show activity",item)
            this.$router.push({
                name:'H5PPlayer',
                params:{ activityId:item.id,idH5Pfile: item.idH5Pfile, activity:item }
            })
        }

    }
  }
</script>

<style scoped>
.v-card__title_dialog {
background-color:#DDEEEB;
font-weight: bold;
justify-content: start;
}
</style>
