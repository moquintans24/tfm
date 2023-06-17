<template>
  <v-layout align-center justify-center>
    <v-flex xs12 sm12 md12>
        <v-container fluid >
          <v-snackbar v-model="showAlert" v-if="showAlert" color="#22555A"
              :timeout="20000"
              elevation="24"
              > {{ $t('class_code_copied') }} 
                <v-icon color="white">mdi-check</v-icon>
          </v-snackbar>
              <v-card style="background: #DDEEEB;background: linear-gradient(to right, #DDEEEB 0%,#CFE7FA 50%, #22555A 100%);">
                <div class="d-flex flex-no-wrap justify-space-between">
                  <v-row
                    align="center"
                    justify="center"
                  >          
                  <v-col cols="10">
                      <v-card-title>
                        <h1 class="font-weight-bold text-h1 v-card__title_">
                          {{ $t('class_group_name') }}: {{classGroup.name}}
                        </h1>
                        
                      </v-card-title>
                      <v-spacer></v-spacer>
                      <v-card-text>
                          <h1 class="font-weight-bold text-h1 v-card__text_">
                            {{book.course}} {{book.level}}
                          </h1>
                      </v-card-text>
                      <v-card-text>
                        <h2 class="font-weight-bold text-h2 blue-grey--text">
                            {{ $t('class_group_code') }}: {{classGroup.code}}
                            <v-btn icon @click.stop="copyToClipBoard(classGroup.code)"  v-blur>
                                <v-icon color="blue" light v-if="!copied">mdi-content-copy</v-icon>
                                <v-icon color="success" light v-if="copied">mdi-check</v-icon>
                            </v-btn> 
                          </h2>
                      </v-card-text>
                  </v-col>     
                </v-row>
                <v-row>
                  <v-col cols="11">
                    <v-img 
                          max-height="180"
                          max-width="130"
                          :src="bookImage"
                          class="ml-auto"
                          ></v-img>
                  </v-col>
                </v-row>  
              </div>
              <v-tabs v-model="activeTab"
                color="#22555A"
                left
              >
                <v-tab :to="studentsTab" v-if="pendingSubscriptions>0"> 
                  <v-badge color="red darken-2" dot  >
                    <span slot="badge" v-if="pendingSubscriptions<=9"> {{ pendingSubscriptions }} </span>
                    <span slot="badge" v-else> 9+ </span>
                      {{ $t('class_users') }}
                  </v-badge>
                </v-tab>
                <v-tab :to="studentsTab" v-else> 
                    {{ $t('class_users') }}
                </v-tab>
                <v-tab :to="activitiesTab">{{ $t('class_activities') }}</v-tab>
                <v-tab :to="gradesTab">{{ $t('users_grades') }}</v-tab>
                <v-tab :to="bookDetailTab">{{ $t('book_content') }}</v-tab>              

                <v-tab-item :value="studentsTab">
                  <ClassUserList />
                </v-tab-item>

                <v-tab-item :value="activitiesTab">
                    <ActivityList :teacherSubscription="teacherSubs"/>
                </v-tab-item>

                <v-tab-item :value="gradesTab">
                  <ClassUserRatings /> 
                </v-tab-item>

                <v-tab-item :value="bookDetailTab">
                    <BookDetail :classGroupBook="book" :fromClassDetail="true"/>
                </v-tab-item>

              </v-tabs>
            </v-card>
        </v-container>
      </v-flex>
    </v-layout>
</template>


<script>
import RepositoryFactory from "@/repositories/RepositoryFactory";
const ClassGroupsRepository = RepositoryFactory.get("classGroups");
const SubscriptionsRepository = RepositoryFactory.get("subscriptions");
import BookDetail from '../book/BookDetail.vue';
import ActivityList from "../activity/ActivityList.vue";
import ClassUserList from "./ClassUserList.vue";
import ClassUserRatings from "./ClassUserRatings.vue";

  export default {
    name: 'ClassDetail',
    components:{
      BookDetail,
      ActivityList,
      ClassUserList,
      ClassUserRatings
    },
    prop:["id"],
    data: () => ({
      book:{},
      classGroup:{},
      teacherSubs:{},
      bookImage:'',
      showAlert:false,
      copied:false,
      copyText:'',
      pendingSubscriptions:0,
      activeTab:null
    }),

    async beforeRouteEnter(to, from, next) {
      const classGroup = await ClassGroupsRepository.getById(to.params.id)
      next(vm => vm.setData(classGroup));
    },
    computed:{
      activitiesTab(){
        return `/classes/${this.$route.params.id}/activities`
      },
      studentsTab(){
        return `/classes/${this.$route.params.id}/students`
      },
      bookDetailTab(){
        return `/classes/${this.$route.params.id}/book`
      },
      gradesTab(){
        return `/classes/${this.$route.params.id}/ratings`
      }
    },
    created() {
      this.$root.$refs.ClassDetail = this;
    },

    methods:{
      setData(classGroup){
        // this.book = classGroup.
        // console.log("book image",classGroup.teacher.code.book)
        this.book = classGroup.teacher.code.book
        this.classGroup = classGroup
        this.teacherSubs = classGroup.teacher
        this.bookImage = 'http://localhost:8080/api/books/images/'+classGroup.teacher.code.book.image
        // console.log("class group",classGroup)
        this.pendingSubscriptions_()
      },
      copyToClipBoard(textToCopy) {
          this.showAlert = true 
          navigator.clipboard.writeText(textToCopy)
          this.copied = !this.copied
          this.copyText = textToCopy 
          setTimeout(()=>{
              this.showAlert = false
              this.copied=false
              this.copyText=''
          },1200)       
              
      },
      async pendingSubscriptions_(){
        await SubscriptionsRepository.getByClassGroupId(this.$route.params.id).then(subscriptions => {
            let count = 0
            for(var i=0;i<subscriptions.length;i++){
              if(subscriptions[i].state == 'PENDING'){
                    count +=1
                    
              }
            }
            this.pendingSubscriptions = count;
            // if(this.pendingSubscriptions == 0){
            //   this.activeTab =  `/classes/${this.$route.params.id}/activities`
            // }else{
            //   this.activeTab =  `/classes/${this.$route.params.id}/students`
            // }
        })
        .catch(err => {
            this.$notify({
            text: err.response.data.message,
            type: "error"
            });
        });
      },
      back(){
        this.$router.go(-1);
      }
    }
  }
</script>


<style scoped>
.v-card__title_ {
    color:#22555A;
    font-weight: bold;
    justify-content: center;
    /* font-family:  cursive; */
}
.v-card__text_ {
    color:#78909C;
    font-weight: bold;
    justify-content: center;
    /* font-family:  cursive; */
}

</style>