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
                                <v-btn v-if="isEditor" class="mx-2" color="#22555A" small dark outlined @click="goToBookDetail()"> 
                                    {{ $t('see_presentation_book') }}        
                                </v-btn>
                            </v-card-actions> 
                        </v-col>  
                    </v-row>
                    <v-row>
                        <v-col cols="11" >
                            <v-img :src="book.image" width="130px" class="ml-auto" 
                                        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                                        height="180px">
                                <v-row class="fill-height ma-0" align="end" justify="center">
                                    <v-btn @click="onButtonClick" class="upload-btn"  dark small fab>
                                        <v-icon>mdi-camera </v-icon>
                                    </v-btn>
                                    <input ref="uploader" class="d-none" type="file" accept="image/*" @change="loadImage">
                                </v-row>
                            </v-img>
                        </v-col>
                    </v-row>  
              </div>
            </v-card>
            
            <v-container>
                <v-layout row wrap >
                    <v-flex xs12 sm12 md12>
                        <v-row justify="end" class="pr-1">
                            <v-btn v-if="isAdmin" class="mx-2" color="#22555A" small dark outlined @click="goToCodeList()"> 
                                {{ $t('codes') }}        
                            </v-btn>
                        </v-row>
                        <v-row  v-if="editableBook" class="pr-1">
                            
                            <v-select v-if="isAdmin || isEditor" class="pl-3 v-select_" v-model="selectedBookState" :items="bookStates" :label="$t('state')" solo 
                            v-on:change="changeBookState()"/>
                            <v-spacer></v-spacer>
                            <div>
                                <v-btn icon v-if="isEditor" class="" color="#22555A">
                                    <v-icon  dark @click="dialogEditTitleBook=true" >mdi-pencil</v-icon>
                                </v-btn>
                                
                                <v-btn v-if="isEditor" class="mx-2" color="#22555A" small dark outlined @click="dialogDuplicateBook=true"> 
                                    <v-icon>mdi-content-duplicate</v-icon>
                                    {{ $t('duplicate') }}        
                                </v-btn>
                                <v-btn v-if="isEditor" class="mx-2" color="red" small dark outlined @click="dialogDelete=true"> 
                                    <v-icon>mdi-delete</v-icon>
                                    {{ $t('delete') }}        
                                </v-btn>
                            </div>
                        </v-row>
                        <v-row justify="end" v-if="isEditor && editableBook" class="pt-1 pr-1">
                            <v-btn class="mx-2" small dark outlined color="success" @click="dialogAddUnit = true"> 
                                <v-icon outlined>mdi-plus</v-icon>
                                {{$t('add')}}    {{$t('unit')}}
                            </v-btn>
                        </v-row>
                        <v-row justify="center">
                            <v-chip-group mandatory column active-class="indigo--text text--darken-4" v-model="indexUnitSelected"> 
                                <v-chip class="ma-2"  v-for="(unit,i) in units" :key="unit.id" color="#DDEEEB" link @click="showLessons(unit.id)" large>
                                    {{$t('unit')}} {{  unit.number }}
                                    <v-btn icon x-small v-if="isEditor && editableBook" color="#22555A" @click.stop="editUnit_(unit), dialogAddUnit=true, editUnit=true">
                                        <v-icon>mdi-pencil</v-icon>
                                    </v-btn>
                                    <v-btn icon color=red x-small v-if="isEditor && editableBook" @click.stop="unitToDelete = unit, dialogDeleteUnit=true">
                                        <v-icon>mdi-delete</v-icon>
                                    </v-btn>
                                </v-chip>
                            </v-chip-group>
                        </v-row>
                        <v-row justify="end" v-if="isEditor && units.length > 0 && editableBook" class="pt-0 pr-1">
                            <v-btn class="mx-2" small dark outlined color="success" @click="dialogAddLesson = true"> 
                                <v-icon outlined>mdi-plus</v-icon>
                                {{$t('add')}}    {{$t('lesson')}}
                            </v-btn>
                        </v-row>
                    </v-flex>
                    <v-expansion-panels class="mt-5" v-model="panel"> 
                        <v-expansion-panel v-for="(lesson,i) in lessons" :key="i" v-if="lesson.unit.id == unitSelected" @click="showActivities(lesson.id)">
                            <v-expansion-panel-header class="v-card__title_lesson">
                                <template>
                                    <v-layout row>
                                        <v-flex xs8 lg10 class="text-left pt-3">{{$t('lesson')}} {{lesson.number}}: {{lesson.name}}</v-flex>
                                        <v-flex  class="text-right pr-6" v-if="isEditor">
                                            
                                            <v-btn  v-if="editableBook" color="#22555A" icon xs-small @click.stop="editLesson_(lesson), dialogAddLesson= true, editLesson = true">
                                                <v-icon small  v-if="editableBook"> mdi-pencil</v-icon>
                                            </v-btn> 
                                            <v-btn  v-if="editableBook"  icon xs-small @click.stop="dialogDeleteLesson = true, lessonToDelete = lesson">
                                                <v-icon small color="red"> mdi-delete</v-icon>
                                            </v-btn> 
                                        </v-flex>
                                    </v-layout>
                                </template>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <v-container fluid>
                                    <v-row justify="end" v-if="isEditor && lessons.length > 0 && editableBook" class="pt-0">
                                        <v-btn class="mx-2" small dark outlined color="success" @click="createActivity(lesson)"> 
                                            <v-icon outlined>mdi-plus</v-icon>
                                            {{$t('add')}}    {{$t('activity')}}
                                        </v-btn>
                                    </v-row>
                                    <v-row>
                                        <v-col 
                                        v-for="(activityItem, index) in activities"
                                        :key="index" cols="12" sm="4" v-if="activityItem.lesson.id == lessonSelected"
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
                                                <v-card-actions>
                                                    <v-spacer></v-spacer>
                                                    <v-btn v-if="isEditor && editableBook" icon small @click.stop="editActivity(activityItem,lesson)" v-blur>
                                                        <v-icon  color="#22555A" dark small>
                                                        mdi-pencil </v-icon>
                                                    </v-btn>
                                                    <v-btn v-if="isEditor && editableBook" icon small @click.stop="dialogDeleteActivity = true, activityToDelete = activityItem" v-blur>
                                                        <v-icon color="red" dark small>
                                                        mdi-delete</v-icon>
                                                    </v-btn>
                                                </v-card-actions>
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
            <v-dialog v-model="dialogDelete" persistent max-width="600px">
                <v-card>
                    <v-card-title class="v-card__title_dialog" >{{$t('delete_book')}} 
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="dialogDelete = false">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-card-text class="v-card__text_dialog">{{$t('question_delete_book')}} </v-card-text>
                        <v-card-actions>
                            <v-btn color="red" text @click.stop="dialogDelete = false">{{ $t('cancel') }}</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-1" text @click.stop="deleteBookConfirm(), dialogDelete = false">{{ $t('ok') }}</v-btn>
                        </v-card-actions>
                    </v-container>  
                </v-card>
            </v-dialog>
            <v-dialog v-model="dialogDuplicateBook" persistent max-width="600px">
                <v-card>
                    <v-card-title class="v-card__title_dialog" >{{$t('duplicate_book')}} 
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="dialogDuplicateBook = false">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-card-text class="v-card__text_dialog">{{$t('question_duplicate_book')}} {{ book.title }}? </v-card-text>
                        <v-card-actions>
                            <v-btn color="red" text @click.stop="dialogDuplicateBook = false">{{ $t('cancel') }}</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-1" text @click.stop="duplicateBook(), dialogDuplicateBook = false">{{ $t('ok') }}</v-btn>
                        </v-card-actions>
                    </v-container>  
                </v-card>
            </v-dialog>
            <v-dialog v-model="dialogDeleteUnit" persistent max-width="600px">
                <v-card>
                    <v-card-title class="v-card__title_dialog" >{{$t('delete_unit')}} 
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="dialogDeleteUnit = false">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-card-text class="v-card__text_dialog">{{$t('question_delete_unit')}} {{ unitToDelete.number }} ?</v-card-text>
                        <v-card-actions>
                            <v-btn color="red" text @click.stop="dialogDeleteUnit = false">{{ $t('cancel') }}</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-1" text @click.stop="deleteUnit(unitToDelete), dialogDeleteUnit = false">{{ $t('ok') }}</v-btn>
                        </v-card-actions>
                    </v-container>  
                </v-card>
            </v-dialog>
            <v-dialog v-model="dialogDeleteLesson" persistent max-width="600px">
                <v-card>
                    <v-card-title class="v-card__title_dialog" >{{$t('delete_lesson')}} 
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="dialogDeleteLesson = false">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-card-text class="v-card__text_dialog">{{$t('question_delete_lesson')}} {{ lessonToDelete.number }}?</v-card-text>
                        <v-card-actions>
                            <v-btn color="red" text @click.stop="dialogDeleteLesson = false">{{ $t('cancel') }}</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-1" text @click.stop="deleteLesson(lessonToDelete), dialogDeleteLesson = false">{{ $t('ok') }}</v-btn>
                        </v-card-actions>
                    </v-container>  
                </v-card>
            </v-dialog>
            <v-dialog v-model="dialogAddUnit" persistent max-width="600px">
                <v-card>
                    <v-card-title v-if="!editUnit" class="v-card__title_dialog" >{{$t('add')}}  {{$t('unit')}}
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="closeForm()">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-card-title v-if="editUnit" class="v-card__title_dialog" >{{$t('edit')}}  {{$t('unit')}}
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="closeForm()">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-form class="pl-4 pr-4"
                            ref="form"
                            v-model="valid"
                            lazy-validation
                        >
                            <v-text-field
                            v-model="newUnit.name"
                            :rules="required_field($t('required_name'))"
                            required
                            ><template #label>
                                {{ $t('name') }}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>

                            <v-text-field
                            v-model="newUnit.number"
                            :rules="required_field($t('required_unit_number'))"
                            required
                            type="number"
                            ><template #label>
                                {{ $t('number') }}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                        </v-form>
                        <v-card-actions>
                            <v-btn color="red" text @click="closeForm()">{{ $t('cancel') }}</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-1" text @click.stop="saveUnit()">{{ $t('ok') }}</v-btn>
                        </v-card-actions>
                    </v-container>  
                </v-card>
            </v-dialog>
            <v-dialog v-model="dialogAddLesson" persistent max-width="600px">
                <v-card>
                    <v-card-title v-if="!editLesson" class="v-card__title_dialog" >{{$t('add')}}  {{$t('lesson')}}
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="closeForm()">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-card-title v-if="editLesson" class="v-card__title_dialog" >{{$t('edit')}}  {{$t('lesson')}}
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="closeForm()">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-form class="pl-4 pr-4"
                            ref="form"
                            v-model="valid"
                            lazy-validation
                        >
                            <v-text-field
                            v-model="newLesson.name"
                            :rules="required_field($t('required_name'))"
                            required
                            ><template #label>
                                {{ $t('name') }}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>

                            <v-text-field
                            v-model="newLesson.number"
                            :rules="required_field($t('required_lesson_number'))"
                            required
                            type="number"
                            ><template #label>
                                {{ $t('number') }}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                        </v-form>
                        <v-card-actions>
                            <v-btn color="red" text @click="closeForm()">{{ $t('cancel') }}</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-1" text @click.stop="saveLesson(),closeForm()">{{ $t('ok') }}</v-btn>
                        </v-card-actions>
                    </v-container>  
                </v-card>
            </v-dialog>
            <v-dialog v-model="dialogDeleteActivity" persistent max-width="600px">
                <v-card>
                    <v-card-title class="v-card__title_dialog" >{{$t('delete_activity')}} 
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="dialogDeleteActivity = false">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-card-text class="v-card__text_dialog">{{$t('question_delete_activity')}} {{ activityToDelete.number }}?</v-card-text>
                        <v-card-actions>
                            <v-btn color="red" text @click.stop="dialogDeleteActivity = false">{{ $t('cancel') }}</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-1" text @click.stop="deleteActivity(activityToDelete), dialogDeleteActivity = false">{{ $t('ok') }}</v-btn>
                        </v-card-actions>
                    </v-container>  
                </v-card>
            </v-dialog>
            <v-dialog v-model="dialogEditTitleBook" persistent max-width="600px">
                <v-card>
                    <v-card-title v-if="!editLesson" class="v-card__title_dialog" >{{$t('edit_book')}}
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="closeForm()">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-form class="pl-4 pr-4"
                            ref="form"
                            v-model="valid"
                            lazy-validation
                        >
                            <v-text-field
                            v-model="book.title"
                            :rules="required_field($t('required_title'))"
                            required
                            ><template #label>
                                {{ $t('title') }}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>

                            <v-text-field
                            v-model="book.course"
                            :rules="required_field($t('required_course'))"
                            required
                            type="text"
                            ><template #label>
                                {{ $t('course') }}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                            <v-text-field
                            v-model="book.level"
                            :rules="required_field($t('required_level'))"
                            required
                            type="number"
                            ><template #label>
                                {{ $t('level') }}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                        </v-form>
                        <v-card-actions>
                            <v-btn color="red" text @click="closeForm()">{{ $t('cancel') }}</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-1" text @click.stop="saveBook()">{{ $t('ok') }}</v-btn>
                        </v-card-actions>
                    </v-container>  
                </v-card>
            </v-dialog>
          </v-container>
      </v-flex>
    </v-layout>
</template>



<script>
import auth from "../../common/auth";
import RepositoryFactory from "@/repositories/RepositoryFactory";
const BooksRepository = RepositoryFactory.get("books");
const UnitsRepository = RepositoryFactory.get("units");
const LessonsRepository = RepositoryFactory.get("lessons");
const ActivityRepository = RepositoryFactory.get("activities");

export default {
    name: 'EditBook',
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
        checkedUnits:[],
        checkedLessons:[],
        unitSelected:1,
        lessonSelected: null,
        isActivityEnabled:true,
        activityIdSelected:null,
        panel:null,
        editableBook: false,
        dialogDelete:false,
        dialogDuplicateBook:false,
        dialogAddUnit: false,
        editUnit: false,
        dialogDeleteUnit: false,
        newUnit:{},
        valid:false,
        unitToDelete:{},
        dialogAddLesson:false,
        dialogDeleteLesson:false,
        editLesson: false,
        newLesson:{},
        lessonToDelete:{},
        lessonToUpdate:{},
        addActivity:false,
        dialogDeleteActivity:false,
        activityToDelete:{},
        dialogEditTitleBook:false,
        bookStates:[],
        selectedBookState:'',
        originalBookState:'',
        indexUnitSelected:null
    }),
    async beforeRouteEnter(to, from, next) {
      //const book = await BooksRepository.getById(to.params.id);
      if(to.params.id){
        next(vm => vm.setData(to.params.id));
      }
      if(to.params.editBook){
        next(vm=> vm.editBook(to.params.editBook))
      }
      if(to.params.unitSelectedFromStudent){
        next(vm => vm.setUnitSelected(to.params.unitSelectedFromStudent));
      }
      if(to.params.lessonSelectedFromStudent){
        next(vm => vm.setLessonSelected(to.params.lessonSelectedFromStudent));
      }
      
    },
    created(){
        if(this.classGroupBook){
            this.setData(this.classGroupBook.id)
        }     
        // console.log(this.$router.currentRoute.path)
        if(this.$router.currentRoute.path.includes('edit')){
            this.editBook(true)
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
        }
    },
    methods: {
        required_field(msg){
            return [value => !!value || msg]
        },
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
        setUnitSelected(unit){
            this.unitSelected = unit.id
        },
        setLessonSelected(lesson){
                this.lessonSelected = lesson.id 
                this.panel = lesson.id - 1
        },
        async setData(bookId) {
            // console.log("bookId",bookId)
            const t = this.$t.bind(this)
            
            try {
                const book = await BooksRepository.getById(bookId)
                if(book != null){
                    // console.log(book.state)
                    this.book = book
                    this.book.image = this.getRouteImage(book.image)
                    if(book.state == 'PENDING'){
                        this.originalBookState = t('book_state_pending')
                        this.selectedBookState = t('book_state_pending')
                        this.bookStates.push(t('book_state_completed'))
                        this.bookStates.push(t('book_state_pending'))
                    }
                    if(book.state == 'COMPLETED'){
                        this.originalBookState = t('book_state_completed')
                        this.selectedBookState = t('book_state_completed')
                        this.bookStates.push(t('book_state_pending'))
                        this.bookStates.push(t('book_state_completed'))
                    }
                    
                    try {
                        const units = await UnitsRepository.getByBookId(bookId)
                        // console.log(units)
                        if(units.length>0){
                            this.units = units
                            try {
                                const lessons_ = new Array()
                                this.unitSelected = units[0].id
                                for(var i=0;i<units.length;i++){
                                    // console.log("units",units)
                                    
                                    await LessonsRepository.getByUnitId(units[i].id).then(lessons =>{
                                        // console.log("lessons", lessons)
                                        for(var j=0;j<lessons.length;j++){
                                            lessons_.push(lessons[j])
                                        }
                                    })
                                    
                                }
                                if(lessons_.length>0){
                                    try {
                                        const activities_ = new Array()
                                        for(var k=0;k<lessons_.length;k++){
                                            await ActivityRepository.getByLessonId(lessons_[k].id).then(async activities =>{
                                                // console.log("activities", activities)
                                                
                                                for(var j=0;j<activities.length;j++){
                                                    const activity_ = new Object()
                                                    activity_.code = activities[j].code
                                                    activity_.description = activities[j].description
                                                    activity_.id = activities[j].id
                                                    activity_.number = activities[j].number
                                                    activity_.idH5Pfile = activities[j].idH5Pfile
                                                    activity_.imagePath = 'http://localhost:8080/api/activities/images/'+activities[j].imagePath
                                                    activity_.lesson = activities[j].lesson
                                                    activity_.students = activities[j].students
                                                    activity_.title = activities[j].title
                                                    activity_.typeH5P = activities[j].typeH5P
                                                    
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
                                this.lessons = lessons_
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
        editBook(){
            this.editableBook = true
        },
        back(){
            this.$router.go(-1);
        },
        async deleteBookConfirm(){
            await BooksRepository.delete(this.$route.params.id).then(() => {
                this.$router.replace({
                    name: "BookList"
                })
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        async duplicateBook(){
            // console.log("duplicate book")
            await BooksRepository.duplicate(this.$route.params.id).then(() => {
                this.$router.push({
                    name: "BookList",
                })
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        closeForm(){
            if(this.dialogEditTitleBook){
                
                this.$refs.form.resetValidation()
                this.dialogEditTitleBook = false
                return;
            }
            if(this.dialogAddUnit){
                this.dialogAddUnit = false
                this.$refs.form.resetValidation()
                if(this.editUnit){
                    this.newUnit ={}
                    this.editUnit = false
                    return;
                }
                // this.$refs.form.resetValidation()
                this.$refs.form.reset()
            }
            if(this.dialogAddLesson){
                this.dialogAddLesson = false
                this.$refs.form.resetValidation()
                if(this.editLesson){
                    this.newLesson ={}
                    this.editLesson = false
                    return;
                }
                // this.$refs.form.resetValidation()
                this.$refs.form.reset()
            }
            
        },
        async saveUnit(){
            if(!this.$refs.form.validate()){
                return;
            }
            let formData = new FormData()

            formData.append('number', this.newUnit.number)
            formData.append('name', this.newUnit.name)

            await UnitsRepository.save(this.$route.params.id,formData).then((unit) => {
                this.units.push(unit)
                this.dialogAddUnit = false
                // console.log(this.units)
                const find = this.units.find(u => u.id == unit.id)
                const index = this.units.indexOf(find)
                this.unitSelected = unit.id
                this.indexUnitSelected = index
                this.closeForm()
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
            
            

        },
        editUnit_(unit){
            this.newUnit = unit
        },
        async deleteUnit(unit){
            
            await UnitsRepository.deleteById(unit.id).then(() => {
                this.dialogDeleteUnit = false
                this.itemIndex = this.units.indexOf(unit)
                this.units.splice(this.itemIndex,1)
                const find = this.units.find(u => u.id == unit.id)
                const index = this.units.indexOf(find)
                this.unitSelected = this.units[0].id
                this.indexUnitSelected = index
                const lessons_ = this.lessons
                for(var i=0; i<lessons_.length;i++){
                    
                    if(lessons_[i].unit.id == unit.id){
                        const index = lessons_.indexOf(lessons_[i])
                        lessons_.splice(index)
                    }
                }
                this.lessons = lessons_
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        async saveLesson(){
            // console.log("unit selected", this.unitSelected)
            // console.log("save lesson",this.editLesson)
            if(!this.$refs.form.validate()){
                return;
            }
            if(!this.editLesson){
                let formData = new FormData()
                formData.append('number', this.newLesson.number)
                formData.append('name', this.newLesson.name)
                
                await LessonsRepository.save(this.unitSelected,formData).then((lesson) => {
                    this.lessons.push(lesson)
                    this.dialogAddLesson = false
                    this.closeForm()
                }).catch(err => {
                    this.$notify({
                        text: err.response.data.message,
                        type: "error"
                    });
                });
            }else{
                // console.log("lesson to update", this.lessonToUpdate)
                await LessonsRepository.update(this.lessonToUpdate).then(() => {
                    this.dialogAddLesson = false
                    this.closeForm()
                }).catch(err => {
                    this.$notify({
                        text: err.response.data.message,
                        type: "error"
                    });
                });
            }
            
        },
        async deleteLesson(lesson){
            await LessonsRepository.deleteById(lesson.id).then(() => {
                this.dialogDeleteLesson = false
                this.itemIndex = this.lessons.indexOf(lesson)
                this.lessons.splice(this.itemIndex,1)
                this.panel =null
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        editLesson_(lesson){
            this.newLesson = lesson
            this.lessonToUpdate = lesson
        },
        createActivity(lesson){
            this.$router.push({
                name:'AddActivity',
                params: { bookId: this.$route.params.id,lessonId:lesson.id }
            })
        },
        editActivity(activity,lesson){
            // console.log("editActivity",activity)
            this.$router.push({
                name:'AddActivity',
                params: { bookId: this.$route.params.id,lessonId:lesson.id,activityId:activity.id,activity:activity }
            })
        },
        async deleteActivity(activity){
            const contentId = activity.idH5Pfile;
            if(contentId){
                const activities = await ActivityRepository.getByIdH5PFile(contentId);
                //Si tenemos más de una actividad asociada a ese id h5p, no podemos eliminar el fichero
                if(activities.length <= 1){
                    const result = await fetch(`/h5p/${contentId}`, {
                        method: 'delete'
                    });
                    if (!result.ok) {
                        throw new Error(
                            `Error while deleting content: ${result.status} ${
                                result.statusText
                            } ${await result.text()}`
                        );
                    }
                }
            }
            await ActivityRepository.deleteById(activity.id).then(() => {
                this.dialogDeleteActivity = false
                this.itemIndex = this.activities.indexOf(activity)
                this.activities.splice(this.itemIndex,1)
            }).catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        goToBookDetail(){
            this.$router.push({name: 'BookDetail', params: { id: this.book.id, book:this.book }})
        },
        loadImage(event) {
            var input = event.target;
            if (input.files) {
                var reader = new FileReader();
                reader.onload = (e) => {
                this.usrImage = e.target.result
                }
                this.image=input.files[0];
                
                let formData = new FormData();

                formData.append('file', input.files[0]);
                formData.append('name', this.book.title);
                formData.append('type', 'book');
                formData.append('id', this.book.id);

                this.updateImage_(formData)
                
                reader.readAsDataURL(input.files[0]);
            }
        },
        async updateImage_(formData){
            await BooksRepository.updateImage(formData).then(img => {
                this.setData(this.book.id)
                if(img!=null){
                    this.book.image = this.getRouteImage(img)     
                } 
            })
        },
        onButtonClick() {
            this.isSelecting = true;
            window.addEventListener(
                "focus",
                () => {
                this.isSelecting = false
                },
                { once: true }
            );
            this.$refs.uploader.click()
            
        },
        async saveBook(){
            // console.log("save book",this.book)
            
            await BooksRepository.update(this.book).then(() => {
                    this.$refs.form.resetValidation()
                    this.dialogEditTitleBook = false
                }).catch(err => {
                    this.$notify({
                        text: err.response.data.message,
                        type: "error"
                    });
                });
        },
        async changeBookState(){
            // console.log(this.selectedBookState, this.originalBookState)
            const t = this.$t.bind(this)
            if(this.originalBookState != this.selectedBookState){
                if(this.selectedBookState == t('book_state_pending')){
                    this.book.state = 'PENDING'
                }else if(this.selectedBookState == t('book_state_completed') ){
                    this.book.state = 'COMPLETED'
                }
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

.v-chips{
  justify-content: center;
}

.v-card__text_dialog {
    font-weight: bold;
    text-align: start;
}
.v-card__title_dialog {
background-color:#DDEEEB;
font-weight: bold;
justify-content: start;
}

.v-select_{
    min-width:20px;
    max-width:250px
}
.upload-btn{
  position: absolute !important;
    z-index: 999;
    top: 121px;
    background: linear-gradient(50deg, #EDFFFC 0%, #203c3d 72%);
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

