<template>
    <v-layout align-center justify-center>
      <v-flex xs12 sm12 md10>
          <v-container fluid >
              
              <v-card style="background: #DDEEEB; background: linear-gradient(to right, #DDEEEB 0%,#CFE7FA 50%, #22555A 100%);" >
                <v-list-item three-line>
                    <v-list-item-content>
                        <v-card-title class="v-card__title_"> {{ $t('class_groups') }} </v-card-title>
                    </v-list-item-content>
                    <v-btn @click="dialogAddClass = true" color="white" fab small dark outlined  v-if="isStudent">
                        <v-icon  >mdi-plus</v-icon>
                    </v-btn>
                    <v-btn  color="white" fab small dark outlined  v-if="isTeacher">
                        <v-icon @click="dialog2 = true">mdi-plus</v-icon>
                    </v-btn>
                    <NewClass v-if="isTeacher" :dialog2="dialog2" :isHomePage="false" :editClass="editClass" :editClassGroup="editClassGroup" @close-dialog="dialog2 =false, editClass=false, editClassGroup=null"/>
              </v-list-item>
              </v-card>
              <v-snackbar v-model="showAlert" v-if="showAlert" color="#22555A"
                    :timeout="20000"
                    elevation="24"
                    > 
                    {{ $t('class_code_copied') }} 
                       
                        <v-icon color="white">mdi-check</v-icon>
                
                </v-snackbar>
                <v-alert v-if="classes.length == 0 && isTeacher"
                    border="top" class="mt-2"
                    colored-border
                    type="info"
                    elevation="2"
                    >
                    {{ $t('no_classes_created') }}
                </v-alert>
                <v-alert v-if="classes.length == 0 && !isTeacher"
                    border="top" class="mt-2"
                    colored-border
                    type="info"
                    elevation="2"
                    >
                    {{ $t('no_classes_assigned') }}
                </v-alert>
              
              <v-row >         
                  <v-col cols="6" v-for="class_ in classes"
                  :key="class_.title" 
                  >
                  <v-card class="mb-2" tile="" @click.stop="goToClassDetail(class_)"
                        style="background-color: #dee5e1;border: 1px solid #d4e3eb;" >
                        
                        <v-row align="start">
                            
                             
                            <v-col >
                                <v-card-title>
                                    <h3 class="font-weight-bold v-card__title_">
                                         {{class_.book.course.toUpperCase()}} {{class_.book.level}}
                                    </h3>
                                    
                                </v-card-title>
                                <v-card-text>
                                    <h3 class="font-weight-bold v-card__text_">
                                        {{class_.name}}
                                    </h3>
                                </v-card-text>
                                <v-card-text v-if="isStudent">
                                    <span v-if="class_.stateSubscriptionStyle == 'red'" style="color:red;">{{ class_.subscriptionState }}</span>
                                    <span v-else-if="class_.stateSubscriptionStyle == 'green'" style="color:green;">{{ class_.subscriptionState }}</span>
                                    <span v-else style="color:orange;">{{ class_.subscriptionState }}</span>
                                    <!-- <h3 class="font-weight-bold v-card__text_">
                                        {{class_.subscriptionState}}
                                    </h3> -->
                                </v-card-text>
                                <v-card-text v-if="isTeacher" class="black--text">
                                    {{ $t('code') }}  {{class_.code}} 
                                    <v-btn icon @click.stop="copyToClipBoard(class_.code)"  v-blur>
                                        <v-icon color="blue" light v-if="copied || copyText!=class_.code">mdi-content-copy</v-icon>
                                        <v-icon color="success" light v-if="!copied && copyText==class_.code">mdi-check</v-icon>
                                    </v-btn> 
                                </v-card-text>
                            </v-col>
                            <v-col class="shrink" >
                                
                                <v-img :src="class_.book.image" max-height="160" max-width="110" class="mr-3"></v-img>
                                <v-card-actions v-if="isTeacher">
                                    
                                    <v-spacer></v-spacer>
                                    <v-btn icon small @click.stop="editClass = true, dialog2=true, editClassGroup = class_" >
                                        <v-icon color="#22555A">mdi-pencil</v-icon>
                                    </v-btn>
        
                                    <v-btn icon small @click.stop="deleteClass(class_),dialogDelete = true">
                                        <v-icon color="red">mdi-delete</v-icon>
                                    </v-btn>
                                </v-card-actions>
                                
                            </v-col>   
                        </v-row>
                    </v-card>
                  </v-col>
                  
              </v-row>
              <v-dialog v-model="dialogDelete" persistent max-width="600px">
                <v-card>
                    <v-card-title class="v-card__title_dialog" >{{$t('delete_class')}} 
                        <v-spacer></v-spacer>
                        <v-btn icon  @click.stop="dialogDelete = false">
                            <v-icon>mdi-close</v-icon>
                        </v-btn>
                    </v-card-title>
                    <v-container>
                        <v-card-text class="v-card__text_dialog">{{$t('delete_class_question')}} {{ deletedClass.name }}? </v-card-text>
                        <v-card-actions>
                            <v-btn color="red" text @click.stop="dialogDelete = false">{{ $t('cancel') }}</v-btn>
                            <v-spacer></v-spacer>
                            <v-btn color="green darken-1" text @click.stop="deleteItemConfirm(), dialogDelete = false">{{ $t('ok') }}</v-btn>
                        </v-card-actions>
                    </v-container>  
                </v-card>
            </v-dialog>
            <NewClassGroupSubscription v-if="isStudent" :dialogAddClass="dialogAddClass" :isHomePage="false" :isBookPage="false" 
                                        @close-dialog="dialogAddClass = false, classGroupCode = null,bookCode=null"/>
          </v-container>
      </v-flex>
    </v-layout>
</template>
  
  
<script>
import auth from "../../common/auth";
import store from "../../common/store";
import NewClass from './NewClass';
import NewClassGroupSubscription from "./NewClassGroupSubscription.vue";
import RepositoryFactory from "@/repositories/RepositoryFactory";
const ClassGroupsRepository = RepositoryFactory.get("classGroups");
const SubscriptionsRepository = RepositoryFactory.get("subscriptions");

export default {
    name: 'ClassList',
    components:{
        NewClass,
        NewClassGroupSubscription
    },
    data: () => ({
        user: store.state.user,
        classes: [],
        dialogDelete:false,
        deletedClass:{},
        copyText:'',
        copied:true,
        search:'',
        dialog: false,
        dialog2:false,
        dialogAddClass:false,
        formHasErrors: false,
        showAlert:false,
        editClass:false,
        editClassGroup:null,
        classGroupCode:null,
        valid:true,
        bookCode:null
    }),

    async beforeRouteEnter(to, from, next) {
        const classGroups = await ClassGroupsRepository.getByUserId(store.state.user.id);
        next(vm => vm.setData(classGroups));
    },
    computed: {
        isAdmin() {
            return auth.isAdmin();
        },
        isTeacher() {
            return auth.isTeacher();
        },
        isStudent() {
            return auth.isStudent();
        }
    },
    methods: {
        copyToClipBoard(textToCopy) {
            this.showAlert = true 
            navigator.clipboard.writeText(textToCopy)
            this.copied = !this.copied
            this.copyText = textToCopy 
            setTimeout(()=>{
                this.showAlert = false
                this.copied=true
                this.copyText=''
            },1000)       
               
        },
        required_field(msg){
            return [value => !!value || msg]
        },
        async setData(data){
            const t = this.$t.bind(this)
            const classes_ = new Array()
            for(var i = 0; i < data.length ; i++){
                const class_ = new Object()
                class_.id = data[i].id
                class_.code = data[i].code
                class_.name = data[i].name
                class_.book = data[i].teacher.code.book
                class_.book.image = 'http://localhost:8080/api/books/images/'+data[i].teacher.code.book.image
                if(auth.isStudent()){
                    await SubscriptionsRepository.getByClassGroupIdAndUserId(data[i].id,store.state.user.id).then(subscription => {
                        class_.subscriptionState = subscription.state
                        switch (subscription.state) {
                            case 'COMPLETED':
                                class_.subscriptionState = t('subscription_completed_state')
                                class_.stateSubscriptionStyle = 'green'
                                break;
                            case 'CANCELLED':
                                class_.subscriptionState = t('subscription_cancelled_state')
                                class_.stateSubscriptionStyle = 'red'
                                break;
                            case 'PENDING':
                                class_.subscriptionState = t('subscription_pending_state')
                                class_.stateSubscriptionStyle = 'orange'
                                break;
                            default:
                                break;
                        }
                    })
                }
                classes_.push(class_)
            }
            this.classes = classes_
        },
        deleteClass(item){
            // console.log("delete class", item)
            this.deletedClass = item
        },
        async deleteItemConfirm(){
            
            await ClassGroupsRepository.delete(this.deletedClass.id)

            const itemIndex = this.classes.indexOf(this.deletedClass)
            this.classes.splice(itemIndex,1)
            // console.log("Clase eliminada", this.deletedClass)
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
.v-card__title_ {
    color:#22555A;
    font-weight: bold;
    /* font-family:  cursive; */
    justify-content: center;
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
.v-card__text_ {
    color:#78909C;
    font-weight: bold;
    justify-content: center;
    /* font-family:  cursive; */
    /* padding: 0 24px 24px; */
}


</style>
    