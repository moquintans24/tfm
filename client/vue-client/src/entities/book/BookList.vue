<template>
    <v-layout align-center justify-center>
        <v-flex xs12 sm12 md10>
            <v-container fluid grid-list-lg>
                <v-card style="background: #DDEEEB; background: linear-gradient(to right, #DDEEEB 0%,#CFE7FA 50%, #22555A 100%);" >
                    
                    <v-list-item three-line>
                        <v-list-item-content>
                            <v-card-title class="v-card__title_"> {{ $t('books') }} </v-card-title>
                        </v-list-item-content>
                        <v-dialog
                            v-model="dialog"
                            persistent
                            max-width="600px"
                            >
                            <template v-slot:activator="{ on, attrs }">
                                <v-btn v-if="!isAdmin && !isEditor" color="white" fab small  outlined v-bind="attrs"  v-on="on">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </template>
                            <v-card >
                                
                                <v-card-title class="v-card__title_dialog" > {{ $t('add_book') }}
                                    <v-spacer></v-spacer>
                                    <v-btn icon  @click="cancel">
                                        <v-icon>mdi-close</v-icon>
                                    </v-btn>
                                </v-card-title>
                                <v-container>
                                    <v-card-text class="v-card__text_dialog"  > {{ $t('add_code_book') }}</v-card-text>
                                    <v-card-text>
                                        <v-row>
                                            <v-col>
                                                <v-form ref="form" v-model="valid" lazy-validation>
                                                    <v-text-field v-model="bookCode" solo
                                                    ref="bookCode"
                                                    :error-messages="errorMessages"
                                                    :rules="required_field($t('required_code'))"
                                                    required
                                                    >{{ $t('code') }}</v-text-field>
                                                </v-form>
                                            </v-col>
                                        </v-row>
                                    </v-card-text>  
                                    <v-card-actions>
                                        <v-btn color="red" text @click="cancel">{{ $t('cancel') }}</v-btn>
                                        <v-spacer></v-spacer>
                                        <v-btn color="#1D8383" text @click="addBookCode" >
                                            {{ $t('add') }}
                                        </v-btn>
                                    </v-card-actions>
                                </v-container>
                            </v-card>
                        </v-dialog>
                    </v-list-item>
                </v-card>
                <v-alert v-if="alert" class="mt-10" border="top" colored-border type="info" elevation="2" >
                    {{ $t('no_books_subscriptions') }}
                </v-alert>
                <v-layout wrap v-if="!alert" >
                    <v-flex xs3 md3 v-if="isAdmin || isEditor">
                        <v-select
                         v-model="selectedBookState.name" 
                        :items="bookStates" :label="$t('state')" solo v-on:change="filterBooksList()"
                        >
                        </v-select>
                        
                    </v-flex>
                    <v-flex xs3 md3>
                        <v-select
                            v-model="selectedLevel"
                            :items="levels"
                            :label="$t('level')"
                            color="#22555A" clearable
                            solo v-on:change="filterBook()"
                        ></v-select>
                    </v-flex>
                    <v-flex xs3 md3>
                        <v-select
                            v-model="selectedCourse"
                            :items="courses"
                            :label="$t('course')"
                            color="#22555A" clearable
                            solo v-on:change="filterBook()"
                        ></v-select>
                    </v-flex>
                    <v-flex xs3 md3>
                        <v-select
                            v-model="selectedTitle"
                            :items="titles"
                            :label="$t('title')"
                            color="#22555A" clearable
                            solo v-on:change="filterBook()"
                        ></v-select>
                    </v-flex>
                </v-layout>
                <v-dialog
                v-model="dialog2"
                max-width="600px"
                >
                <v-card>
                    <v-card-title class="v-card__title_dialog"> {{ $t('create_new_class') }}
                    <v-spacer></v-spacer>
                    <v-btn icon  @click="closeCreateClass">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-card-text class="text-h5 font-weight-bold" > 
                            {{ $t('text_new_class') }}  
                        </v-card-text>
                        <v-card-text>
                        <v-spacer></v-spacer>
                        <v-form ref="form2" v-model="valid" lazy-validation>
                            <v-row>
                                <v-col>
                                    <div style="text-align:left" > {{ $t('class_group_name') }} <span class="red--text"><strong>* </strong></span></div>
                                    <v-text-field 
                                        v-model="className"
                                        ref="className"
                                        :rules="required_field($t('required_class_name'))"
                                        solo
                                        required
                                    >
                                </v-text-field>
                                </v-col>
                                <v-col cols="12">
                                    <div style="text-align:left" > {{ $t('book') }} </div>
                                    <v-text-field type="text" solo
                                        :value="cardTitle" readonly
                                        ></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6">
                                    <div style="text-align:left" > {{ $t('course') }} </div>
                                    <v-text-field
                                    :value="cardCourse"
                                    solo
                                    readonly
                                    ></v-text-field>
                                </v-col>
                                <v-col cols="12" sm="6" >
                                    <div style="text-align:left" > {{ $t('level') }} </div>
                                    <v-text-field 
                                        :value="cardLevel"
                                        solo readonly
                                    ></v-text-field>
                                </v-col>
                            </v-row>
                        </v-form>
                        </v-card-text>
                        <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn
                                color="#1D8383"
                                text
                                @click="addNewClass"
                            >{{ $t('create') }}
                            </v-btn>
                        </v-card-actions>
                    </v-container>
                </v-card>  
                </v-dialog>
                <v-dialog
                    v-model="dialogToDeleteBook"
                    persistent
                    max-width="600px"
                    >
                    <v-card>
                        <v-card-title class="v-card__title_dialog">  {{ $t('delete_book') }}
                            <v-spacer></v-spacer>
                            <v-btn icon  @click="dialogToDeleteBook = false">
                                <v-icon>mdi-close</v-icon>
                            </v-btn>
                        </v-card-title>
                        
                        <v-container>
                            <v-card-text class="v-card__text_dialog"> {{ $t('question_delete_book') }}</v-card-text>
                            <v-card-actions>
                                <v-btn
                                    color="red"
                                    text
                                    @click="dialogToDeleteBook = false"
                                >
                                {{ $t('cancel') }}
                                </v-btn>
                                <v-spacer></v-spacer>
                                <v-btn
                                    color="green darken-1"
                                    text
                                    @click="deleteItemConfirm()"
                                >
                                {{ $t('ok') }}
                                </v-btn>
                            </v-card-actions>
                        </v-container>
                        
                    </v-card>
                    </v-dialog>
                    
                    <v-layout row wrap>
                        <v-flex class="flex-card" v-for="(card, index) in bookList" :key="card.id" > 
                            <v-hover v-slot="{ hover }">
                                <v-card :style="isStudent ?  { 'width': '300px'} : isTeacher ? {'width':'450px'} : {'width': '220px'}" class="book">
                                    <v-row v-if="isTeacher">
                                        <v-col>
                                            <v-img
                                                :src="card.image"
                                                class="white--text align-end ml-2"
                                                gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                                                :style="isStudent ? { 'height': '320px'} :  {'width': '220px', 'height':'290px'}">
                                                <v-expand-transition>
                                                <div
                                                    v-if="hover"
                                                    class="d-flex transition-fast-in-fast-out darken-2 v-card--reveal text-h2 white--text"
                                                    style="height: 100%;"
                                                >
                                                    <v-btn :to="{name: 'BookDetail', params: { id: card.id, book:card }}"
                                                        color="white"
                                                        > {{ $t('see_book') }}
                                                    </v-btn>
                                                </div>
                                                </v-expand-transition>
                                            <v-card-title v-text="card.title"></v-card-title>
                                            </v-img>
                                        </v-col>
                                        <v-col >
                                            <v-card-title class="v-card__class_title_"> {{ $t('class_groups') }}</v-card-title>
                                            <v-alert v-if="card.classGroups.length==0" dense text type="info" elevation="2" class="mx-2 pa-2">
                                                {{ $t('no_class_groups_created') }}
                                            </v-alert>
                                            <v-chip-group column  v-for="classGroup in card.classGroups" :key="classGroup.id" >
                                                <v-chip  @click.stop="goToClassDetail(classGroup)" color="#DDEEEB" link>
                                                    <strong>{{ $t('name') }}: </strong> {{ classGroup.name }}
                                                </v-chip>
                                            </v-chip-group>
                                        </v-col>
                                    </v-row>

                                    <v-img v-else
                                        :src="card.image"
                                        class="white--text align-end"
                                        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                                        :style="isStudent ? { 'height': '320px'} :  {'width': '220px', 'height':'290px'}">
                                        <v-expand-transition>
                                        <div
                                            v-if="hover"
                                            class="d-flex transition-fast-in-fast-out darken-2 v-card--reveal text-h2 white--text"
                                            style="height: 100%;"
                                        >
                                            <v-btn :to="{name: 'BookDetail', params: { id: card.id, book:card }}"
                                                color="white"
                                                > {{ $t('see_book') }}
                                            </v-btn>
                                        </div>
                                        </v-expand-transition>
                                    <v-card-title v-text="card.title"></v-card-title>
                                    </v-img>                                    
                                    <v-card-text>
                                        <p class="text-h4 text--primary ">
                                            <strong>{{ $t('course') }}: </strong> {{card.course}} - 
                                            <strong>{{ $t('level') }}: </strong> {{card.level}}
                                        </p>
                                    </v-card-text>
                                    
                                    <v-card-actions >
                                        
                                        <v-btn v-if="isAdmin" x-small color="#DDEEEB" :to="{ name: 'CodeList', params: { id: card.id } }"> 
                                            {{ $t('codes') }}
                                        </v-btn>
                                        <v-spacer></v-spacer>
                                        <v-btn v-if="isTeacher" x-small color="#DDEEEB" @click.stop="createNewClass(card.title,card.course,card.level,card)"> 
                                            {{ $t('create_class_group') }}
                                        </v-btn>
                                        <v-btn icon v-if="isEditor  && card.state != 'READY'" :to="{name: 'EditBook', params: { id: card.id, book:card, editBook:true }}">
                                            <v-icon color="#22555A">mdi-pencil</v-icon>
                                        </v-btn>
                                        <v-btn v-if="(isAdmin || isEditor) && card.state != 'READY'" icon  @click.stop="deleteBook(card)">
                                            <v-icon color="red">
                                                mdi-delete
                                            </v-icon>
                                        </v-btn>
                                    </v-card-actions>
                                </v-card>
                            </v-hover>
                        </v-flex>
                        <v-pagination v-if="bookList.length >5" class="pagination mb-2" v-model="page" :length="pages" :total-visible="5" color="#22555A" circle @input="updatePage"></v-pagination>
                    </v-layout>
            </v-container>
        </v-flex>
    </v-layout>
</template>


<script>
import auth from "../../common/auth";
import store from "../../common/store";
import RepositoryFactory from "@/repositories/RepositoryFactory";
const BooksRepository = RepositoryFactory.get("books");
const SubscriptionsRepository = RepositoryFactory.get("subscriptions");
const ClassGroupRepository = RepositoryFactory.get("classGroups");

export default {
    name: 'BookList',
    data: () => ({
        valid:true,
        alert:false,
        books: [],
        booksFiltered: [],
        search:'',
        dialog: false,
        dialog2:false,
        dialogToDeleteBook:false,
        idBookToRemove:null,
        bookCode:null,
        errorMessages: '',
        formHasErrors: false,
        className:'',
        cardTitle:'',
        cardLevel:null,
        cardCourse:'',
        itemIndex:-1,
        page: 1,
		pageSize: 12,
        bookListCount: 0,
		bookList: [],
        bookStates:[],
        selectedBookState:{
            name:'',
            originName:''
        },
        bookStatesWithOriginName:[],
        subscriptions:[],
        bookToCreateClass:{},
        showAlert:false,
        levels:[],
        courses:[],
        titles:[],
        selectedCourse:null,
        selectedLevel:null,
        selectedTitle:null
    }),
    async beforeRouteEnter(to, from, next) {
        
        if(auth.isAdmin() || auth.isEditor()){
            const books = await BooksRepository.getAll();
            next(vm => vm.setData(books));
        }else {
            const subscriptions = await SubscriptionsRepository.getByUserId(store.state.user.id);
            const books = []
            for(var i=0;i<subscriptions.length;i++){
                if(subscriptions[i].code!=null){
                    books.push(subscriptions[i].code.book)
                }else if(subscriptions[i].classGroup!=null){ //Alumnos que se suscriben a una clase, no tienen código de libro
                    books.push(subscriptions[i].classGroup.teacher.code.book) 
                }
            }
            next(vm => vm.setData(books, subscriptions));
        }
        if(to.params.code){
            next(vm => vm.addBookWithCodeInUrl(to.params.code));
        }
      
    },
    computed: {
        isAdmin() {
            return auth.isAdmin();
        },
        isEditor() {
            return auth.isEditor();
        },
        isTeacher() {
            return auth.isTeacher();
        },
        isStudent(){
            return auth.isStudent();
        },
        pages() {
			
			if (this.pageSize == null || this.bookListCount == null) return 0
			return Math.ceil(this.bookListCount / this.pageSize)
		},
        form () {
            return {
                bookCode: this.bookCode
                }
            },
        form2 () {
            return {
                className: this.className
                }
            },
        },
        
    watch: {
        bookCode () {
            this.errorMessages = ''
        },
    },
    methods: {
        async setData(data, subscriptions) {
            if(!auth.isAdmin() && !auth.isEditor()){
                this.subscriptions = subscriptions
            }
            // console.log("data",data)
            const t = this.$t.bind(this)
            const books_ = new Array()
            const states = new Array()
            if(data.length > 0){
                for(var i = 0; i < data.length; i++){
                    const b = new Object()
                    b.id = data[i].id
                    b.title = data[i].title 
                    b.course = data[i].course
                    b.level = data[i].level
                    b.state = data[i].state
                    b.image = this.getRouteImage(data[i].image)
                    

                    if(auth.isTeacher()){
                        const subscription = this.subscriptions.find(s => s.code.book.id == data[i].id)
                        const classGroups = await ClassGroupRepository.getByBookIdAndTeacherSubscriptionId(data[i].id,subscription.id)
                        b.classGroups = classGroups
                    }
                    
                    books_.push(b)
                    // console.log("books_",books_)
                    const bookState = new Object()
                    switch (data[i].state) {
                        case 'READY':
                            states.push(t('book_state_ready'))
                            bookState.name = t('book_state_ready')
                            bookState.originName = data[i].state
                            if(!auth.isEditor){
                                this.selectedBookState.name = t('book_state_ready')
                                this.selectedBookState.originName = data[i].state
                            }
                            this.bookStatesWithOriginName.push(bookState)
                            
                            break;
                        case 'PENDING':
                            states.push(t('book_state_pending'))
                            bookState.name = t('book_state_pending')
                            if(auth.isEditor){
                                this.selectedBookState.name = t('book_state_pending')
                                this.selectedBookState.originName = data[i].state
                            }
                            bookState.originName = data[i].state
                            this.bookStatesWithOriginName.push(bookState)
                            break;
                        case 'COMPLETED':
                            states.push(t('book_state_completed'))
                            bookState.name = t('book_state_completed')
                            bookState.originName = data[i].state
                            this.bookStatesWithOriginName.push(bookState)
                            break;
                        case 'ARCHIVED':
                            states.push(t('book_state_archived'))
                            bookState.name = t('book_state_archived')
                            bookState.originName = data[i].state
                            this.bookStatesWithOriginName.push(bookState)
                            break;    
                    
                        default:
                            break;
                    }
                    
                }
                // console.log("states", states)
                if(!states.find(state => state == t('book_state_pending'))) {
                    // this.showAlert = true 
                    this.selectedBookState.name = t('book_state_ready')
                    this.selectedBookState.originName = 'READY'
                }    
                this.bookStatesWithOriginName = [...new Map(this.bookStatesWithOriginName.map((item) => [item["name"], item])).values()]
                this.books = books_
            
                this.bookStates = [...new Set(states)];
                
                this.filterBooksList()
            }else{
                this.alert = true
            }
            
            
           
        },
        getRouteImage(linkImage){
            return 'http://localhost:8080/api/books/images/'+linkImage
        },
        required_field(msg){
            return [value => !!value || msg]
        },
        initPage() {
            this.bookListCount = this.booksFiltered.length
            
			if (this.bookListCount < this.pageSize) {
                this.bookList = this.booksFiltered
			} else {
                this.bookList = this.booksFiltered.slice(0, this.pageSize)
			}
		},
		updatePage(pageIndex) {
			
			let _start = (pageIndex - 1) * this.pageSize
			let _end = pageIndex * this.pageSize
		
            this.bookList = this.booksFiltered.slice(_start, _end)
            
			this.page = pageIndex
		},
        filterBooksList(){
            if(this.selectedBookState.name){
                const result = this.bookStatesWithOriginName.find(o => o.name === this.selectedBookState.name)
                this.selectedBookState.originName = result.originName
                const levels = new Array()
                const courses = new Array()
                const titles = new Array()

                const books_ = this.books.filter(book => 
                    book.state == this.selectedBookState.originName
                )
                for(var i=0;i<books_.length;i++){
                    levels.push(books_[i].level)
                    courses.push(books_[i].course)
                    titles.push(books_[i].title)
                }
                this.booksFiltered = books_     
                // console.log("filter books",this.levels)
                this.courses = courses
                this.levels = levels
                this.titles = titles

                this.initPage()
                this.updatePage(this.page)  
            }
                 
        },
        filterBook(){
            if(this.selectedLevel != null && this.selectedCourse != null && this.selectedTitle != null){
                this.bookList = this.booksFiltered.filter(book => 
                        book.level == this.selectedLevel && book.course == this.selectedCourse && book.title == this.selectedTitle                    
                )
            }else if(this.selectedLevel != null && this.selectedCourse != null){
                this.bookList = this.booksFiltered.filter(book => 
                        book.level == this.selectedLevel && book.course == this.selectedCourse
                    
                )
            }else if(this.selectedLevel != null && this.selectedTitle != null){
                this.bookList = this.booksFiltered.filter(book => 
                        book.level == this.selectedLevel && book.title == this.selectedTitle
                )
            }else if(this.selectedTitle != null && this.selectedCourse != null){
                this.bookList = this.booksFiltered.filter(book => 
                        book.course == this.selectedCourse && book.title == this.selectedTitle
                )
            }else if(this.selectedTitle != null){
                this.bookList = this.booksFiltered.filter(book => 
                        book.title == this.selectedTitle
                )
            }else if(this.selectedCourse != null){
                this.bookList = this.booksFiltered.filter(book => 
                        book.course == this.selectedCourse
                )
            }else if(this.selectedLevel != null){
                this.bookList = this.booksFiltered.filter(book => 
                        book.level == this.selectedLevel
                )
            }else{
                this.bookList = this.booksFiltered
            }
        },
        async addBookWithCodeInUrl(code){
            this.dialog = true
            this.bookCode = code
        },
        async addBookCode(){
            if (!this.$refs.form.validate()) {
                return
            }
            let formData = new FormData();
            formData.append("code",this.bookCode)
            formData.append("userId",store.state.user.id)
            
            await SubscriptionsRepository.save(formData).then(async() => {
                if(this.$route.params.code){
                    this.dialog = false
                    this.$refs.form.reset()
                    if(this.$route.params.code){
                        this.$router.push({
                            path: '/books'
                        })
                    }

                    const subscriptions = await SubscriptionsRepository.getByUserId(store.state.user.id);
                    const books = []
                    for(var i=0;i<subscriptions.length;i++){
                        if(subscriptions[i].code!=null){
                            books.push(subscriptions[i].code.book)
                        }else if(subscriptions[i].classGroup!=null){ //Alumnos que se suscriben a una clase, no tienen código de libro
                            books.push(subscriptions[i].classGroup.teacher.code.book) 
                        }
                    } this.setData(books,subscriptions)
                }else{
                    this.$router.go({
                        name: "BookList"
                    })
                }
                
            }).catch(err => {
              this.$notify({
                text: err.response.data.message,
                type: "error"
              });
            });
            
            
        },
        async addNewClass(){
            // console.log("Add new class", this.bookToCreateClass)
            if (!this.$refs.form2.validate()) {
                return
            }
            const classGroup = new Object()
            classGroup.name = this.className
            
            const subscription = this.subscriptions.find(element => element.code.book.id == this.bookToCreateClass.id)
            classGroup.teacher = subscription //Teacher subscription
            // console.log(classGroup)
            await ClassGroupRepository.save(classGroup).then(() => {
                            this.$router.push({
                                name: "ClassList"
                            })
                        })
                        .catch(err => {
                            this.$notify({
                                text: err.response.data.message,
                                type: "error"
                            });
                        });
        },
        createNewClass(title, course, level,book){
            this.dialog2 = true
            this.cardTitle = title
            this.cardCourse = course
            this.cardLevel =level
            
            this.bookToCreateClass = book
           
        },
        closeCreateClass(){
            this.dialog2 = false
            this.$refs.form2.reset()
       
        },
        cancel(){
            this.dialog = false
            this.$refs.form.reset()
            if(this.$route.params.code){
                this.$router.push({
                    path: '/books'
                })
            }
        },
        closeRemoveBook(){
            this.dialogToDeleteBook = false
        },
        deleteBook(item){
            this.dialogToDeleteBook = true
            this.itemIndex = this.bookList.indexOf(item)
            this.idBookToRemove = item.id            
        },  
        async deleteItemConfirm(){
            await BooksRepository.delete(this.idBookToRemove)
            .then( () => {
                this.dialogToDeleteBook = false
                this.bookList.splice(this.itemIndex,1)
                if(!this.bookList.find(b => b.state === this.selectedBookState.originName)){
                    this.$router.go({
                        name:'BookList'
                    })
                    const index = this.bookStates.indexOf(this.selectedBookState.name)
                    this.bookStates.splice(index,1)
                    this.selectedBookState.originName = ''
                    this.selectedBookState.name = ''
                }
                this.$notify({ 
                    text: 'Libro eliminado',
                    type: "success"
                })
                
            })
            .catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type:"error"
                })
            }) 
        },
        async goToClassDetail(classGroup){
            if(auth.isTeacher()){
                this.$router.push({
                    name: 'ClassDetail',
                    params: { id: classGroup.id }
                })
            }else {
                const subscription = await SubscriptionsRepository.getByClassGroupIdAndUserId(classGroup.id,store.state.user.id)
                this.$router.push({
                    name: 'ClassDetailStudent',
                    params: { id: classGroup.id,subscriptionId: subscription.id }
                })
            }
            
        }
    }
};
</script>

<style scoped>
.my-select .v-input__control .v-input__slot {
  padding: 0 !important;
  margin-top: 200px ;
  min-height: 10px;
  display:flex;
  align-items: center;
  min-width: 6px;
}
.v-card__title_ {
    color:#22555A;
    font-weight: bold;
    justify-content: center;
}

.v-card__class_title_ {
    color:#22555A;
    font-weight: bold;
}

.v-card__title_dialog {
background-color:#DDEEEB;
font-weight: bold;
justify-content: start;
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

.v-card__text_dialog {
    font-weight: bold;
    text-align: start;
}

.flex-card {
  flex-grow: 0;
}

.book:hover {
  transition: all linear 200ms;
  box-shadow: 9px 9px 9px rgba(20,20,20,0.6);
}
.div_{
    background-color: #DDEEEB;
}
</style>
    