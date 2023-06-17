<template>
  <v-layout align-center justify-center>
    <v-flex xs12 sm12 md12>
        <v-container fluid >
              <v-card style="background: #DDEEEB; background: linear-gradient(to right, #DDEEEB 0%,#CFE7FA 50%, #22555A 100%);">
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
              
              <v-tabs v-if="!pendingSubscription && !cancelledSubscription"
                color="#22555A"
                left
              >
              
                <v-tab :to="pendingActivities" >{{ $t('pending_activities') }}</v-tab>
                <v-tab :to="allActivities" >{{ $t('explore_activities') }}</v-tab>

                <v-tab-item :value="pendingActivities">
                    <PendingActivityList />
                </v-tab-item>

                <v-tab-item :value="allActivities">
                  <ActivityListByUser :subscriptionId="userSubscription.id" :classGroupId="classGroup.id"/> 
                </v-tab-item>
              </v-tabs>
            </v-card>
            <v-alert v-if="pendingSubscription" dense text type="warning"  elevation="2" class="mt-5">
                {{ $t('pending_subscription') }}
            </v-alert>
            <v-alert v-if="cancelledSubscription" dense text type="error"  elevation="2" class="mt-5">
                {{ $t('cancelled_subscription') }}
            </v-alert>
        </v-container>
      </v-flex>
    </v-layout>
</template>


<script>
import RepositoryFactory from "@/repositories/RepositoryFactory";
const ClassGroupsRepository = RepositoryFactory.get("classGroups");
const SubscriptionsRepository = RepositoryFactory.get("subscriptions");
import BookDetail from '../book/BookDetail.vue';
import PendingActivityList from "../activity/PendingActivityList.vue";
import ActivityListByUser from "../activity/ActivityListByUser.vue";
import store from "../../common/store";

  export default {
    name: 'ClassDetailStudent',
    components:{
      BookDetail,
      PendingActivityList,
      ActivityListByUser
    },

    data: () => ({
      book:{},
      classGroup:{},
      teacherSubs:{},
      bookImage:'',
      userSubscription:{},
      pendingSubscription:false,
      cancelledSubscription: false
    }),

    async beforeRouteEnter(to, from, next) {
      const classGroup = await ClassGroupsRepository.getById(to.params.id)
      next(vm => vm.setData(classGroup));
    },
    computed:{
      pendingActivities(){
        return `/classes/${this.$route.params.id}/student/${this.$route.params.subscriptionId}/pending-activities`
      },
      allActivities(){
        return `/classes/${this.$route.params.id}/student/${this.$route.params.subscriptionId}/activities`
      },
    },
    methods:{
      async setData(classGroup){
        // this.book = classGroup.
        const subscription = await SubscriptionsRepository.getById(this.$route.params.subscriptionId);
        if(subscription.state=='PENDING'){
          this.pendingSubscription = true
        }else if(subscription.state == 'CANCELLED'){
          this.cancelledSubscription = true
        }
        // console.log("book image",classGroup.teacher.code.book)
        this.book = classGroup.teacher.code.book
        this.classGroup = classGroup
        this.teacherSubs = classGroup.teacher
        this.bookImage = 'http://localhost:8080/api/books/images/'+classGroup.teacher.code.book.image

        this.userSubscription = await SubscriptionsRepository.getByClassGroupIdAndUserId(classGroup.id,store.state.user.id)
        // console.log("class group",classGroup)
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