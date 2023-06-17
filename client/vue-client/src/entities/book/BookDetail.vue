<template>
    <v-layout align-center justify-center>
    <v-flex xs12 sm12 md10 >
        <v-container fluid >
            <v-card v-if="!fromClassDetail" style="background: #DDEEEB; background: linear-gradient(to right, #DDEEEB 0%,#CFE7FA 50%, #22555A 100%);">
                <div class="d-flex flex-no-wrap justify-space-between">
                    <v-row
                        align="center"
                        justify="center"
                    >          
                        <v-col cols="10">
                            <v-card-title>
                                <h1 class="font-weight-bold text-h1 v-card__title_">
                                    {{ book.title }}
                                </h1>
                                
                            </v-card-title>
                            <v-spacer></v-spacer>
                            <v-card-text>
                                <h1 class="font-weight-bold text-h1 v-card__text_">
                                    {{book.course}} {{book.level}}
                                </h1>
                            </v-card-text>
                            <v-card-actions class="">
                                <v-btn v-if="isEditor && book.state == 'PENDING'" class="mx-2" color="#22555A" small dark outlined @click="goToEditBook()"> 
                                    {{ $t('see_edition_book') }}        
                                </v-btn>
                                <v-btn v-if="isStudent && !pendingSubscription" class="mx-2" color="#22555A" small dark outlined @click="dialogAddClass = true"> 
                                    {{ $t('subscription_class_group') }}        
                                </v-btn>
                                
                            </v-card-actions> 
                        </v-col>     
                    </v-row>
                    <v-row>
                        <v-col cols="11">
                            <v-img 
                                max-height="180"
                                max-width="130"
                                :src="book.image"
                                class="ml-auto"
                                ></v-img>
                        </v-col>
                    </v-row>
              </div>
            </v-card>
            <NewClassGroupSubscription v-if="isStudent" :dialogAddClass="dialogAddClass" :isHomePage="false" :isBookPage="true" 
                                        @close-dialog="dialogAddClass = false, classGroupCode = null"/>
            <v-alert v-if="pendingSubscription" dense text type="warning"  elevation="2" class="mt-5">
                {{ $t('pending_subscription') }}
            </v-alert>
            <v-alert v-if="cancelledSubscription" dense text type="error"  elevation="2" class="mt-5">
                {{ $t('cancelled_subscription') }}
            </v-alert>
            <v-alert v-if="showStudentAlert" dense text type="info" elevation="2" class="mt-12" >
                {{ $t('no_content_visible') }}
            </v-alert>
            <v-container>
                
                <v-layout row wrap >
                    <v-flex xs12 sm12 md12>
                        <div class="d-flex justify-space-between mb-6 bg-surface-variant" v-if="isAdmin && (book.state == 'COMPLETED' || book.state == 'READY' || book.state == 'ARCHIVED')">
                            <v-select   class="v-select_" v-model="selectedBookState" :items="bookStates" :label="$t('state')" solo 
                            v-on:change="changeBookState()"/>
                            <v-btn color="#22555A" small dark outlined @click="goToCodeList()"> 
                                {{ $t('codes') }}        
                            </v-btn>
                        </div>
                        <v-row justify="end" class="pr-1" v-if="isAdmin && book.state == 'PENDING'">
                            <v-btn class="mx-2" color="#22555A" small dark outlined @click="goToCodeList()"> 
                                {{ $t('codes') }}        
                            </v-btn>
                        </v-row>
                        <v-row justify="center">
                            <v-chip-group mandatory column active-class="indigo--text text--darken-4" v-model="indexUnitSelected"> 
                                <v-chip class="ma-2" v-for="(unit,i) in units" :key="unit.id" color="#DDEEEB" link @click="showLessons(unit.id)" x-large>
                                    {{$t('unit')}} {{  unit.number }} 
                                </v-chip>
                            </v-chip-group>
                        </v-row>
                    </v-flex>
                    <v-expansion-panels class="mt-5" v-model="panel"> 
                        <v-expansion-panel v-for="(lesson,i) in lessons" :key="i" v-if="lesson.unit.id == unitSelected" @click="showActivities(lesson.id)">
                            <v-expansion-panel-header class="v-card__title_lesson">
                                <template>
                                    <v-layout row>
                                        <v-flex xs8 lg10 class="text-left pt-3">{{$t('lesson')}} {{lesson.number}}: {{lesson.name}}</v-flex>
                                        <v-flex class="text-right pr-6" v-if="isTeacher && fromClassDetail">
                                            <v-tooltip top v-if="lesson.id <= untilLesson.id">
                                                <template v-slot:activator="{ on, attrs }">
                                                    <v-btn icon xs-small v-bind="attrs"  v-on="on" @click.stop="showHideLesson(lesson,'HIDE')" v-blur>
                                                        <v-icon  color="#22555A" dark> mdi-eye </v-icon>
                                                    </v-btn>
                                                </template>
                                                <span>{{$t('hide')}}</span>
                                            </v-tooltip>
                                            <v-tooltip top v-if="lesson.id > untilLesson.id">
                                                <template v-slot:activator="{ on, attrs }">
                                                    <v-btn icon xs-small v-bind="attrs"  v-on="on" @click.stop="showHideLesson(lesson,'SHOW')" v-blur>
                                                        <v-icon color="#22555A" dark > mdi-eye-off</v-icon>
                                                    </v-btn>
                                                </template>
                                                <span>{{$t('show')}}</span>
                                            </v-tooltip>
                                        </v-flex>
                                    </v-layout>
                                </template>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <v-container fluid>
                                    <v-row>
                                        <v-col 
                                        v-for="(activityItem, index) in activities"
                                        :key="index" cols="12" md="4" v-if="activityItem.lesson.id == lessonSelected"
                                        >
                                            <v-hover v-slot="{ hover }">
                                                <v-card color="#ECEFF1" 
                                                    class="mx-auto"
                                                    max-width="400"
                                                >
                                                    <v-img class="white--text align-end" height="220px"
                                                        :src="activityItem.imagePath">
                                                        
                                                        <v-expand-transition>
                                                            <div v-if="hover"
                                                                class="d-flex transition-fast-in-fast-out darken-2 v-card--reveal text-h2 white--text"
                                                                style="height: 100%;" >
                                                                <v-btn @click="openH5PPlayer(activityItem)" color="black" class="white--text"> 
                                                                    
                                                                    <v-icon color="#ffb74d">mdi-play</v-icon>Play
                                                                </v-btn>
                                                            </div>
                                                        </v-expand-transition>
                                                    </v-img>

                                                    <v-card-text class="text--primary">
                                                        <div class="font-weight-bold">{{$t('description')}}: {{activityItem.description}}</div>

                                                        <div>{{$t('code')}}: {{activityItem.code}}</div>
                                                    </v-card-text>
                                                </v-card>
                                            </v-hover>
                                        </v-col>
                                    </v-row>
                                </v-container>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-expansion-panels>                   
                </v-layout>
            </v-container>
          </v-container>
      </v-flex>
    </v-layout>
</template>



<script>
import auth from "../../common/auth";
import RepositoryFactory from "@/repositories/RepositoryFactory";
import store from "../../common/store";
import NewClassGroupSubscription from "../class/NewClassGroupSubscription.vue";
import SubscriptionsRepository from "../../repositories/SubscriptionsRepository";
const BooksRepository = RepositoryFactory.get("books");
const UnitsRepository = RepositoryFactory.get("units");
const LessonsRepository = RepositoryFactory.get("lessons");
const ActivityRepository = RepositoryFactory.get("activities");
const InteractionActivity = RepositoryFactory.get("interactionsActivities");
const ClassGroupsRepository = RepositoryFactory.get("classGroups");

export default {
    name: 'BookDetail',
    components:{
        NewClassGroupSubscription
    },
    props:{
        classGroupBook:{
            type: Object,
            required:false
        },
        fromClassDetail:{
            type: Boolean,
            required:false
        },
        unitSelectedFromStudent:{
            type: Object,
            required:false
        },
        lessonSelectedFromStudent:{
            type: Object,
            required:false
        }
    },
    data: () => ({
        book:{},
        units:[],
        lessons:[],
        activities:[],
        unitOpen: false,
        unitIdOpen:'',
        lessonOpen:false,
        lessonIdOpen:'',
        panel:[],
        checkedUnits:[],
        checkedLessons:[],
        unitSelected:null,
        indexUnitSelected: null,
        lessonSelected: null,
        isActivityEnabled:true,
        activityIdSelected:null,
        interactionsActivities:[],
        panel:null,
        bookStates:[],
        selectedBookState:'',
        originalBookState:'',
        untilLesson:{}, //lección hasta la que están las actividades visibles para los alumnos
        untilUnit:{}, //unidad hasta la que están las actividades visibles para los alumnos
        showStudentAlert:false,
        dialogAddClass:false,
        pendingSubscription:false,
        cancelledSubscription:false
    }),
    async beforeRouteEnter(to, from, next) {
     
      if(to.params.unitSelectedFromStudent){
        next(vm => vm.setUnitSelected(to.params.unitSelectedFromStudent,to.params.bookId));
      }else  if(to.params.lessonSelectedFromStudent){
        next(vm => vm.setLessonSelected(to.params.lessonSelectedFromStudent,to.params.bookId));
      }else  if(to.params.id){
        next(vm => vm.setData(to.params.id))
      }
      
    },
    async created(){
        if(this.classGroupBook){
            this.setData(this.classGroupBook.id)
        }     
        if(auth.isStudent()){
            await SubscriptionsRepository.getByBookIdAndUserId(this.$route.params.id,store.state.user.id).then(subscription =>{
                if(subscription.state == 'PENDING') this.pendingSubscription = true
                if(subscription.state == 'CANCELLED') this.cancelledSubscription = true
            });
        }
    },
    computed:{
        isEditor() {
            return auth.isEditor();
        },
        isAdmin() {
            return auth.isAdmin();
        },
        isTeacher() {
            return auth.isTeacher();
        },
        isStudent(){
            return auth.isStudent();
        }
    },
    methods: {
        checkUnit(unitId) {
            if(this.checkedUnits.includes(unitId)){
                const index = this.checkedUnits.indexOf(unitId);
                if (index > -1) {
                    this.checkedUnits.splice(index, 1);
                }
            }else{
                this.checkedUnits.push(unitId) 
            }
        },
        checkLesson(lessonId) {
            if(this.checkedLessons.includes(lessonId)){
                const index = this.checkedLessons.indexOf(lessonId);
                if (index > -1) {
                    this.checkedLessons.splice(index, 1);
                }
            }else{
                this.checkedLessons.push(lessonId) 
            }
        },
        setUnitSelected(unit,bookId){
            this.unitSelected = unit.id
            this.setData(bookId)
        },
        setLessonSelected(lesson,bookId){
                this.lessonSelected = lesson.id 
                this.setData(bookId)
        },
        async setData(bookId) {
            
            
            const t = this.$t.bind(this)
            let studentClassGroups = []
            if(this.fromClassDetail){
                await ClassGroupsRepository.getById(this.$route.params.id).then(classGroup => {
                    if(classGroup.lesson){
                        this.untilLesson = classGroup.lesson
                        this.untilUnit = classGroup.lesson.unit
                    }else{
                        this.untilLesson.id = 0
                        this.untilUnit.id = 0
                    }
                }).catch(err => {
                    this.$notify({
                        text: err.response.data.message,
                        type: "error"
                    });
                });
            }
            
            if(auth.isStudent()){
                await ClassGroupsRepository.getByBookIdAndUserId(this.$route.params.id,store.state.user.id).then(classGroups =>{
                    //En caso de que el estudiante tenga suscripción a un grupo de clase, la lista solo tendrá un objecto grupo de clase
                    studentClassGroups = classGroups
                    if(classGroups.length > 0){
                        if(classGroups[0].lesson){
                            this.untilLesson = classGroups[0].lesson
                            this.untilUnit = classGroups[0].lesson.unit
                        }else{
                            this.showStudentAlert = true
                        }
                    }
                }).catch(err => {
                    this.$notify({
                        text: err.response.data.message,
                        type: "error"
                    });
                });
            }
            try {
                const book = await BooksRepository.getById(bookId)
                if(book != null){
                    this.book = book
                    this.book.image = this.getRouteImage(book.image)
                    switch (book.state ) {
                        case 'COMPLETED':
                            this.originalBookState = t('book_state_completed')
                            this.selectedBookState = t('book_state_completed')
                            this.bookStates.push(t('book_state_ready'))
                            this.bookStates.push(t('book_state_completed'))
                            break;
                        case 'READY':
                            this.originalBookState = t('book_state_ready')
                            this.selectedBookState = t('book_state_ready')
                            this.bookStates.push(t('book_state_archived'))
                            this.bookStates.push(t('book_state_ready'))
                            break;
                        case 'ARCHIVED':
                            this.originalBookState = t('book_state_archived')
                            this.selectedBookState = t('book_state_archived')
                            this.bookStates.push(t('book_state_archived'))
                            this.bookStates.push(t('book_state_ready'))
                            break;
                    
                        default:
                            break;
                    }
                    // if(book.state == 'COMPLETED'){
                    //     this.originalBookState = t('book_state_completed')
                    //     this.selectedBookState = t('book_state_completed')
                    //     this.bookStates.push(t('book_state_ready'))
                    //     this.bookStates.push(t('book_state_completed'))
                    // }
                    // if(book.state == 'READY'){
                    //     this.originalBookState = t('book_state_ready')
                    //     this.selectedBookState = t('book_state_ready')
                    //     this.bookStates.push(t('book_state_archived'))
                    //     this.bookStates.push(t('book_state_ready'))
                    // }
                    try {
                        const units = await UnitsRepository.getByBookId(bookId)
                        // console.log("units", units)
                        if(units.length>0){
                            //Los estudiantes verán solo las unidades que están habilitadas, en caso de que estén suscritos a un grupo de clase
                            if(auth.isStudent() && studentClassGroups.length > 0){ 
                                const units_ = new Array()
                                for(var l=0;l<units.length;l++){
                                    
                                    if(units[l].id <= this.untilUnit.id){
                                        units_.push(units[l])
                                    }
                                }
                                this.units = units_
                            }else{
                                this.units = units
                            }
                            
                            try {
                                const lessons_ = new Array()
                                // console.log("lessons")
                                for(var i=0;i<this.units.length;i++){                                    
                                    await LessonsRepository.getByUnitId(units[i].id).then(lessons =>{ 
                                        for(var j=0;j<lessons.length;j++){
                                            //Los estudiantes verán solo las unidades que están habilitadas, en caso de que estén suscritos a un grupo de clase
                                            if(auth.isStudent() && studentClassGroups.length > 0){ 
                                                if(lessons[j].id <= this.untilLesson.id){
                                                    lessons_.push(lessons[j])
                                                }
                                            }else{
                                                lessons_.push(lessons[j])
                                            }  
                                        }  
                                    })
                                    
                                }

                                this.lessons = lessons_
                                
                                if(this.$route.params.unitSelectedFromStudent) {
                                    const find = this.units.find(u => u.id == this.$route.params.unitSelectedFromStudent.id)
                                    const index = this.units.indexOf(find)
                                    this.unitSelected = this.$route.params.unitSelectedFromStudent.id
                                    this.indexUnitSelected = index
                                }else if(this.$route.params.lessonSelectedFromStudent) {
                                    
                                    this.lessonSelected = this.$route.params.lessonSelectedFromStudent.id
                                    const unitFind = this.units.find(u => u.id == this.$route.params.lessonSelectedFromStudent.unit.id)
                                    const unitIndex = this.units.indexOf(unitFind)
                                    this.unitSelected = this.$route.params.lessonSelectedFromStudent.unit.id
                                    this.indexUnitSelected = unitIndex
                                  
                                    const lessonsByUnit = this.lessons.filter(l => l.unit.id == this.unitSelected)
                                    const find = lessonsByUnit.find(l => l.id == this.$route.params.lessonSelectedFromStudent.id)
                                    const index = lessonsByUnit.indexOf(find)
                                    this.panel = index
                                }else{
                                    this.unitSelected = Object.values(this.units)[0].id
                                    // console.log(Object.values(this.units)[0].id)
                                    this.indexUnitSelected = 0
                                }

                                if(lessons_.length>0){
                                    try {
                                        const activities_ = new Array()
                                        for(var k=0;k<lessons_.length;k++){
                                            await ActivityRepository.getByLessonId(this.lessons[k].id).then(async activities =>{
                                                
                                                for(var j=0;j<activities.length;j++){
                                                    const activity_ = new Object()
                                                    activity_.code = activities[j].code
                                                    activity_.description = activities[j].description
                                                    activity_.id = activities[j].id
                                                    activity_.idH5Pfile = activities[j].idH5Pfile
                                                    activity_.imagePath = 'http://localhost:8080/api/activities/images/'+activities[j].imagePath
                                                    activity_.lesson = activities[j].lesson
                                                    activity_.students = activities[j].students
                                                    activity_.title = activities[j].title
                                                    activity_.typeH5P = activities[j].typeH5P
                                                    
                                                    if(!auth.isAdmin() && !auth.isEditor() ){
                                                        const interactions = await InteractionActivity.getByActivityIdAndClassGroupId(activities[j].id, this.$route.params.id)
                                                        // console.log(interactions)
                                                        let intActivity = interactions.find(o => o.id === activity_.id);
                                                        // console.log("intActivity",intActivity!=null)

                                                        if(intActivity != null) {
                                                            activity_.available = true
                                                        }else {activity_.available = false}
                                                    }
                                                    
                                                    // console.log("activity_",activity_)
                                                    activities_.push(activity_)
                                                }
                                            })
                                        }
                                        this.activities = activities_
                                        
                                    } catch (err) {
                                        this.$notify({
                                            text: err.response.data.message,
                                            type:"error"
                                        })
                                    }
                                }
                                
                                // this.activities = activities_
                            } catch (err) {
                                this.$notify({
                                    text: err.response.data.message,
                                    type:"error"
                                })
                            }
                            
                        } 
                    } catch (err) {
                        this.$notify({
                            text: err.response.data.message,
                            type:"error"
                        })
                    }
                }
             
            } catch (err) {
                this.$notify({
                    text: err.response.data.message,
                    type:"error"
                })
            } 
        },
        
        async getInteractionsActivityByActivityId(activityId){
            await InteractionActivity.getByActivityIdAndClassGroupId(activityId, this.$route.params.id).then(intActivities => {
            // console.log("Created see students",intActivities)
                if(intActivities.length>0){
                    const interactionsActivities_ = new Array()
                    for(var i=0;i<intActivities.length;i++){
                        const interactionActivity_ = {
                            activity: new Object(),
                            id: Number,
                            subscription: new Object(),
                            teacherSubscription: new Object()
                        }
                        this.activityCode = intActivities[i].activity.code
                        interactionActivity_.activity = intActivities[i].activity
                        interactionActivity_.id = intActivities[i].id
                        interactionActivity_.duration = intActivities[i].duration
                        const date_ = new Date(intActivities[i].homeworkDate)
                        interactionActivity_.homeworkDate = date_.toLocaleDateString(i18n.locale.toString()).concat(" ",date_.toLocaleTimeString(i18n.locale.toString()))
                        interactionActivity_.score = intActivities[i].score
                        interactionActivity_.subscription.userName = (intActivities[i].subscription.user.name).concat(" ",intActivities[i].subscription.user.lastName)
                    
                        interactionActivity_.subscription.userImagePath = 'http://localhost:8080/api/users/images/'+intActivities[i].subscription.user.linkImage

                        interactionActivity_.teacherSubscription = intActivities[i].teacherSubscription
                        
                        interactionsActivities_.push(interactionActivity_)
                    }
                    this.interactionsActivities = interactionsActivities_


                }
                
            })
            .catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        async createInteractionsActivity(activity){
            // console.log("createInteractionsActivity", activity)
            await InteractionActivity.saveInteractionByActivityIdAndClassGroupId(activity.id, this.$route.params.id)

        },
        getRouteImage(linkImage){
            return 'http://localhost:8080/api/books/images/'+linkImage
        },
        goToCodeList(){
            this.$router.push({
                    name: "CodeList",
                    params: { id: this.book.id }
                })
        },  
        showLessons(unitId){
            this.unitSelected = unitId
        },
        showActivities(lessonId){
            this.lessonSelected = lessonId
        },
        
        back(){
            this.$router.go(-1);
        },
        goToEditBook(){
            this.$router.push({name: 'EditBook', params:{ id: this.book.id, book:this.book, editBook:true }})
        },
        async changeBookState(){
            const t = this.$t.bind(this)
            if(this.originalBookState != this.selectedBookState){
                if(this.selectedBookState == t('book_state_ready') ){
                    
                    this.book.state = 'READY'
                }
                if(this.selectedBookState == t('book_state_archived') )this.book.state = 'ARCHIVED'
                // console.log("change book state", this.book.state)
                await BooksRepository.update(this.book).then(() =>{
                    this.$router.replace({
                        name: "BookList",
                    })
                }).catch(err => {
                    this.$notify({
                        text: err.response.data.message,
                        type: "error"
                    });
                });
            }
        },
        async showHideLesson(lesson, type){
            // console.log("show hide lesson", type, ' ', lesson)
            await ClassGroupsRepository.showHideLesson(this.$route.params.id, lesson, type).then(classGroup=>{
                if(classGroup.lesson){
                    this.untilLesson = classGroup.lesson
                    this.untilUnit = classGroup.lesson.unit
                }else{
                    this.untilLesson.id = -1
                    this.untilUnit.id = -1
                }
                
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        async openH5PPlayer(activity){
            this.$router.push({
                name:'H5PPlayer',
                params:{ activityId:activity.id,idH5Pfile: activity.idH5Pfile, activity:activity  }
            })
            
        }
    },
}
</script>


<style scoped>
.v-card__title_ {
    color:#22555A;
    font-weight: bold;
    justify-content: center;
    /* font-family:  cursive; */
}
.v-card__title_lesson:hover {
    background-color:#DDEEEB;
    justify-content: start;
}
.v-card__activity_description {
    font-size: 15px;
    font-weight: 500;
}
.v-card__text_ {
    color:#78909C;
    font-weight: bold;
    justify-content: center;
    /* font-family:  cursive; */
}



.v-select_{
    min-width:20px;
    max-width:210px;
    max-height: 25px;
    margin-left: 0px;
    padding-left: 0px;
}

.v-card--reveal {
    background-color: #22555A;
    align-items: center;
    bottom: 0;
    justify-content: center;
    opacity: .5;
    position: absolute;
    width: 100%;
}

</style>

