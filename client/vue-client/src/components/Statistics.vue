<template>
    <v-layout align-center justify-center>
        <v-flex xs12 sm12 md10>
            <v-container fluid >
                <v-card>
                    <v-card-title class="v-card__title_" 
                    style="background: #DDEEEB; background: linear-gradient(to right, #DDEEEB 0%,#CFE7FA 50%, #22555A 100%);" height="92px">{{ $t('statistics') }} </v-card-title>
                                        
                    <v-data-table
                        :headers="getHeaders"
                        :items="filteredBooks"
                        :items-per-page="5"
                        :footer-props="{itemsPerPageText: $t('rows_per_page'),
                                        'items-per-page-all-text': $t('all')
                                        }"
                                        
                        class="elevation-1"
                    >
                        <template v-slot:top>
                            <v-toolbar flat  >
                                <v-row >
                                    <v-col>
                                        <v-autocomplete class="pr-5 pt-5"
                                            v-model="selectedLevel" 
                                            :items="levels" clearable
                                            :label="$t('search_by_level')"
                                        ></v-autocomplete>
                                    </v-col>
                                    <v-col>
                                        <v-autocomplete  class="pr-5 pt-5"
                                            v-model="selectedCourse" 
                                            :items="courses" clearable
                                            :label="$t('search_by_course')"
                                        ></v-autocomplete>
                                    </v-col>
                                    <v-col>
                                        <v-autocomplete  class="pr-5 pt-5"
                                            v-model="searchValue" 
                                            :items="titles" clearable
                                            :label="$t('search_by_book_title')"
                                        ></v-autocomplete>
                                    </v-col>
                                    <v-col>
                                        <v-autocomplete  class="pr-5 pt-5"
                                            v-model="selectedBookState" 
                                            :items="bookState" clearable
                                            :label="$t('search_by_book_state')"
                                        ></v-autocomplete>
                                    </v-col>
                                </v-row>
                            </v-toolbar>
                        </template>
                        <template v-slot:item.book_image="{ item }">
                            <img height="150px" class="pa-1"
                                    :src="item.book_image" 
                                >
                        </template>
                        <template v-slot:item.book_title="{ item }">
                            <router-link :to="{ name: 'BookDetail', params: { id: item.book_id } }">
                                {{ item.book_title }}
                            </router-link>
                        </template>
                        <template v-slot:item.book_state="{ item }">
                            <span v-if="item.book_state == $t('book_in_use')" style="color:green;"> {{ item.book_state }} <v-icon color="#a5d6a7">mdi-book-open-page-variant</v-icon></span>
                            <span v-else style="color:orange;">{{ item.book_state }} <v-icon color="#ffcc80">mdi-archive</v-icon></span>
                        </template>
                    </v-data-table>
                </v-card>
            </v-container>
        </v-flex>
    </v-layout>
</template>

<script>
import RepositoryFactory from "@/repositories/RepositoryFactory";
import { title } from "process";
const BooksRepository = RepositoryFactory.get("books");

export default {
    name: 'Statistics',
    data () {
        return {
            booksStatistics: [],
            selectedLevel:null,
            selectedCourse:null,
            courses:[],
            levels:[],
            filteredBooks:[],
            searchValue:null,
            titles:[],
            selectedBookState:null,
            bookState:[]
        }
    },
    computed: {
        getHeaders () {
			const t = this.$t.bind(this)
                return  [
                    { text: t('image'), value: 'book_image', align:'start', sortable:false,class:'black--text' },
                    { text: t('title'), value: 'book_title', align:'start', sortable:true, class:'black--text', filter:this.filterByTitle },
                    { text: t('course'), value: 'book_course', align:'start', sortable:true, class:'black--text', filter:this.filterByCourse },
                    { text: t('level'), value: 'book_level', align:'center', sortable:true, class:'black--text', filter:this.filterByLevel },
                    { text: t('total_subscriptions'), value: 'total_subscriptions', align:'center', sortable:true, class:'black--text' },
                    { text: t('total_class_groups'), value: 'total_class_groups', align:'center', sortable:true, class:'black--text' },
                    { text: t('total_centers'), value: 'total_centers', align:'center', sortable:true, class:'black--text' },
                    { text: t('state'), value: 'book_state', align:'start', sortable:true, class:'black--text',filter:this.filterByBookState }
                ]
        }
    },
    async beforeRouteEnter(to,from,next){
        const statistics = await BooksRepository.getStatistics()
        next(vm => vm.setData(statistics))
    },
    methods: {
        setData(data){
            const t = this.$t.bind(this)
            // console.log(data)
            // this.statistics = data
            const statistics_ = []
            const states_ = new Array()
            const levels_ = new Array();
            const courses_ = new Array();
            const titles_ = new Array()
            for(var i =0; i<data.length;i++){
                
                const value = new Object()
                // console.log(data[i][0])
                value.book_id = data[i][0].id
                value.book_title= data[i][0].title
                value.book_level= data[i][0].level
                value.book_course= data[i][0].course
                value.book_state = data[i][0].state
                value.book_image = this.getRouteImage(data[i][0].linkImage)
                value.total_subscriptions = data[i][1]
                value.total_centers = data[i][2]
                value.total_class_groups = data[i][3]
                //Solo vamos a tener estádisticas de los libros en estado archivado o listo
                switch (data[i][4]) {
                    case 'READY':
                        value.book_state = t('book_in_use')
                        states_.push(t('book_in_use'))
                        break;
                    case 'ARCHIVED':
                        value.book_state = t('book_state_archived')
                        states_.push(t('book_state_archived'))
                        break;
                
                    default:
                        break;
                }
                
                levels_.push(data[i][0].level)
                courses_.push(data[i][0].course)
                titles_.push(data[i][0].title)
                statistics_.push(value)
            }
            this.levels = [...new Set(levels_)].sort()
            this.courses = [...new Set(courses_)].sort()
            this.titles = [...new Set(titles_)].sort()
            this.bookState = [...new Set(states_)].sort()
            this.booksStatistics = statistics_
            // console.log(this.booksStatistics)
            this.filteredBooks = this.booksStatistics

        },
        getRouteImage(linkImage){
            return 'http://localhost:8080/api/books/images/'+linkImage
        },
        filterByCourse(value){
            if(!this.selectedCourse){
                return true
            }
            return value === this.selectedCourse
        },
        filterByLevel(value){
            if (!this.selectedLevel) {
                return true;
        }
            return value===this.selectedLevel;
        },
        filterByTitle(value){
            if (!this.searchValue) {
                return true;
            }
            return value===this.searchValue;
        },
        filterByBookState(value){
            if(!this.selectedBookState){
                return true;
            }
            return value===this.selectedBookState
        }
    },
}
</script>


<style scoped>
.v-card__title_ {
    color:#22555A;
    font-weight: bold;
    justify-content: center;
}
.v-text__field{
    width: 25px;
}
.v-select_{
    min-width:20px;
    max-width:160px;
}

</style>