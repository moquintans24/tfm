<!-- Diálogo para crear y editar grupos de clase  -->
<template>
    <div class="text-center">
        <!-- Solo el profesor va a crear las clases-->
        <v-dialog persistent
        v-model="dialog2"
        max-width="600px"
        >
            <v-card >
                <v-card-title class="v-card__title_dialog" v-if="!editClass"> {{ $t('create_new_class') }}
                    <v-spacer></v-spacer>
                    <v-btn icon  @click="closeCreateClass">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-card-title>
                <v-card-title class="v-card__title_dialog" v-if="editClass"> {{ $t('edit_class') }}
                    <v-spacer></v-spacer>
                    <v-btn icon  @click="closeCreateClass">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-card-title>
                <v-container>
                    <v-card-text class="v-card__text_dialog" v-if="!editClass"> 
                        {{ $t('text_new_class_from_classes') }}  
                    </v-card-text>
                    <v-card-text>
                        <v-row>
                            <v-col>
                                <v-form ref="form" v-model="valid" lazy-validation>
                                    <v-autocomplete
                                        v-model="selectedBook"
                                        :items="books"
                                        clearable
                                        required 
                                        :rules="required_field($t('required_select_book'))"     
                                        item-value="id"
                                        return-object                  
                                       >
                                        <template #label>
                                            {{$t('select_book')}}<span class="red--text"><strong>* </strong></span>
                                        </template>
                                        <template v-slot:selection="data">
                                            <v-chip
                                                v-bind="data.attrs"
                                                :input-value="data.selected"
                                                @click="data.select"
                                            > 
                                            <v-avatar left>
                                                <v-img :src="data.item.image"></v-img>
                                            </v-avatar>
                                                {{ data.item.title }} 
                                            </v-chip>
                                        </template>

                                        <template v-slot:item="data">
                                            <v-list-item-avatar>
                                                <img :src="data.item.image">
                                            </v-list-item-avatar>
                                            <v-list-item-content>
                                                <v-list-item-title v-html="data.item.title"></v-list-item-title>
                                                <v-list-item-subtitle>{{data.item.level}}º {{data.item.course}}</v-list-item-subtitle>
                                            </v-list-item-content>
                                        </template>
                                    </v-autocomplete>
                                    <div style="text-align:left" > {{ $t('class_group_name') }} <span class="red--text"><strong>* </strong></span></div>
                                    <v-text-field 
                                        v-model="className"
                                        ref="className"
                                        :rules="required_field($t('required_class_name'))"
                                        solo
                                        required
                                    >
                                    </v-text-field>
                                </v-form>
                            </v-col>
                        </v-row>
                    </v-card-text>  
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn v-if="!editClass"
                            color="#1D8383"
                            text
                            @click="saveClass"
                        >{{ $t('create') }}
                        </v-btn>
                        <v-btn v-if="editClass"
                            color="#1D8383"
                            text
                            @click="saveClass"
                        >{{ $t('edit') }}
                        </v-btn>
                    </v-card-actions> 
                </v-container>
            </v-card>  
        </v-dialog>
    </div>
</template>

<script>
import auth from "../../common/auth";
import store from "../../common/store";
import RepositoryFactory from "@/repositories/RepositoryFactory";
const SubscriptionsRepository = RepositoryFactory.get("subscriptions");
const ClassGroupRepository = RepositoryFactory.get("classGroups");


export default {
    name: 'NewClass',
    props:{
        dialog2: {
            type: Boolean,
            required: true
        },
        isHomePage: {
            type: Boolean,
            required: true
        },
        editClass: {
            type: Boolean,
            required: false
        },
        editClassGroup:{
            type: Object,
            required:false
        }

    },
    data: () => ({
        valid:true,
        className:'',
        selectedBook:'',
        books: [ ],
        showAlert:false,
        subscriptions:[]
    }),
    watch: {
        editClassGroup() {
            this.editClass_();
        }
    },
    computed: {
        isAdmin() {
            return auth.isAdmin();
        }
        
    },
    async created() {
        //Suscripciones del usuario
      const userSubscriptions = await SubscriptionsRepository.getByUserId(store.state.user.id);
      this.subscriptions = userSubscriptions
  
            const books_ = new Array()
            for(var i=0;i<userSubscriptions.length;i++){
                const book_ = new Object()
             
                book_.id = userSubscriptions[i].code.book.id
                book_.title = userSubscriptions[i].code.book.title
                book_.level = userSubscriptions[i].code.book.level
                book_.image = 'http://localhost:8080/api/books/images/'+userSubscriptions[i].code.book.image
                book_.subscriptionId = userSubscriptions[i].id
          
                books_.push(book_)
            }
         
            const unique = (value, index, self) => {
                return self.indexOf(value) === index
            }
            this.books = books_.filter(unique)
    },
    methods: {
        editClass_() {
            if(this.editClassGroup!= null){

                this.className = this.editClassGroup.name
                this.selectedBook = this.books.find(element => element.id == this.editClassGroup.book.id)

            }
        },
        required_field(msg){
            return [value => !!value || msg]
        },
        async saveClass(){

            if (!this.$refs.form.validate()) {
                return
            }
            try {
               
                const classGroup = new Object()
                classGroup.name = this.className
                // console.log("selected book",this.selectedBook)
                
                const subscription = this.subscriptions.find(element => element.id == this.selectedBook.subscriptionId)
                if(this.editClass) {
                    classGroup.id = this.editClassGroup.id
                }
                classGroup.teacher = subscription //Teacher subscription
                // console.log(classGroup)
                this.$emit('close-dialog',false)
                
                
                if(this.isHomePage) {
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
                }else if(!this.isHomePage ) {
                    await ClassGroupRepository.save(classGroup).then(() => {
                                this.$router.go({
                                name: "ClassList"
                            })
                        })
                        .catch(err => {
                            this.$notify({
                                text: err.response.data.message,
                                type: "error"
                            });
                        });
                    
                }
                this.closeCreateClass()

            } catch (err) {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                })
            }
        },
        validate () {
            this.$refs.form.validate()
        },
        resetValidation () {
            this.$refs.form.resetValidation()
        },
        reset () {
            this.$refs.form.reset()
        },
        closeCreateClass(){
            this.$emit('close-dialog',false)       
            this.reset()
        }
    }
};
</script>
    
<style scoped>
.v-card__text_dialog {
    font-weight: bold;
    text-align: start;
}

.v-card__title_dialog {
background-color:#DDEEEB;
font-weight: bold;
justify-content: start;
}
</style>