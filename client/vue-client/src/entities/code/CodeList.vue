<template>
    <v-layout align-center justify-center>
        <v-flex xs12 sm12 md10>
            <v-container fluid >
                <v-card style="background: #DDEEEB; background: linear-gradient(to right, #DDEEEB 0%,#CFE7FA 50%, #22555A 100%);">
                <div class="d-flex flex-no-wrap justify-space-between">
                    <v-row
                        align="center"
                        justify="center"
                    >          
                        <v-col cols="10">
                            <v-card-title>
                                <h3 class="font-weight-bold text-h1 v-card__title_">
                                    {{ $t('codes') }} {{ book.title }}
                                </h3>
                                
                            </v-card-title>
                            <v-spacer></v-spacer>
                            <v-card-text>
                                <h2 class="font-weight-bold text-h1 v-card__text_">
                                    {{book.course}} {{book.level}}
                                </h2>
                            </v-card-text>
                            <v-card-actions >
                                
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
                <v-card>
                    <v-data-table
                        :headers="getHeaders"
                        :items="codes"
                        :items-per-page="10"
                        :search="search"
                        :footer-props="{itemsPerPageText: $t('rows_per_page'),
                                        'items-per-page-all-text': $t('all')
                                        }"
                                        
                        class="elevation-1">
                        <template v-slot:top>
                            <v-toolbar flat>
                                <v-btn class="ma-2" color="#22555A" small dark outlined @click="(dialog = true, isButtonCreateCodes=false)">
                                    <v-icon left>mdi-export-variant</v-icon> {{ $t('export') }}
                                </v-btn>
                                
                                <v-spacer></v-spacer>
                                <v-btn class="mx-2" color="#22555A" small dark outlined @click="(dialog = true, isButtonCreateCodes=true)"> 
                                    {{ $t('create_codes' )}}
                                </v-btn>
                            </v-toolbar>
                        </template>
                        <template v-slot:item.state="{ item }">
                            <span v-if="item.state == $t('code_state_new')" style="color:green;"> {{ item.state }}</span>
                            <span v-else style="color:orange;">{{ item.state }}</span>
                        </template>
                    </v-data-table>
                    
                </v-card>
                <ExportCodes v-if="dialog" :dialog="dialog" :isButtonCreateCodes="isButtonCreateCodes" :bookId="book.id" :book="book" @close-dialog="dialog =false"/>
          </v-container>
      </v-flex>
    </v-layout>
</template>


<script>
import auth from "../../common/auth";
import RepositoryFactory from "@/repositories/RepositoryFactory";
import ExportCodes from './DialogExportCodes.vue'
const CodesRepository = RepositoryFactory.get("codes");
const BooksRepository = RepositoryFactory.get("books");

// Carga dinámica de datos antes de cargar la ruta:
// https://router.vuejs.org/guide/advanced/data-fetching.html#fetching-before-navigation

export default {
    name: 'CodeList',
    components:{
        ExportCodes
    },
    data: () => ({
        codes: [],
        valid:true,
        search:'',
        dialog:false,
        book:{},
        isButtonCreateCodes:false
    }),
    async beforeRouteEnter(to, from, next) {
        const codes = await CodesRepository.getByBookId(to.params.id)
        const book = await BooksRepository.getById(to.params.id)
        next(vm => vm.setData(codes,book));
    },
    async created() {
        this.dialog = false
        await CodesRepository.getByBookId(this.$route.params.id);
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
                        { text: t('code'), align: 'start', sortable: true, value: 'code', class: 'black--text' },
                        { text: t('create_date'), align: 'start', sortable: true, value: 'creationDate', class: 'black--text' },
                        { text: t('expiration_date'), align: 'start', sortable: true, value: 'expirationDate', class: 'black--text' },
                        { text: t('generated_by'), align: 'start', value: 'generatedBy', sortable: true, class:"black--text"},
                        { text: t('permissions'), value: 'authority', align: 'start', sortable: true, class:"black--text"},
                        { text: t('state'), value: 'state', align: 'start', sortable: true, class:"black--text"}
                    ]
        }
    },
    // created(){
    //     this.dialog = false
    // },
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
        setData(data,book) {
            this.book = book;
            this.book.image = 'http://localhost:8080/api/books/images/'+book.image
            const t = this.$t.bind(this)
            const codes_ = new Array()
            for(var i = 0; i < data.length; i++){
                const cds = new Object()
                cds.code = data[i].code
                cds.creationDate = data[i].creationDate.substring(0, 10).concat(' ',data[i].creationDate.substring(11,16))
                cds.expirationDate = data[i].expirationDate.substring(0, 10).concat(' ',data[i].expirationDate.substring(11,16))
                cds.generatedBy = data[i].generatedBy.login
                switch (data[i].state) {
                    case 'USED':
                        cds.state = t('code_state_used')
                        break;
                    case 'NEW':
                        cds.state = t('code_state_new')
                        break;
                    default:
                        break;
                }
                
                cds.authority = data[i].generatedBy.authority
                codes_.push(cds)
            }
            this.codes = codes_.sort((a,b) => new Date(b.creationDate).getDate() - new Date(a.creationDate).getDate()).reverse();
        },
        back(){
            this.$router.go(-1);
        }
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

.v-text__field{
      width: 25px;
}

.v-card__text_ {
    color:#78909C;
    font-weight: bold;
    justify-content: center;
    /* font-family:  cursive; */
}
</style>
    
  