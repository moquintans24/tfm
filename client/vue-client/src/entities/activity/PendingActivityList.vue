<template>
    <v-layout align-center justify-center>
        <v-flex xs12 sm12 md12>
            <v-container fluid >
                
                <v-alert v-if="showAlert" dense text type="success" elevation="2" >
                    {{ $t('no_pending_activities') }}
                </v-alert>
                <v-row>         
                  <v-col cols="6" v-for="intAct in interactionsActivities"
                  :key="intAct.id" 
                  >
                  <v-card class="mb-2" tile="" 
                        style="background-color: #dee5e1;border: 1px solid #d4e3eb;" >
                                <v-card-title  class="justify-center pt-1 pb-0">
                                    <h5 class="font-weight-bold v-card__text__">
                                        {{ $t('activity') }} {{intAct.activity.number}} 
                                    </h5>
                                </v-card-title>
                        <v-row align="start" >
                            <v-hover v-slot="{ hover }">
                                <v-col class="shrink" >
                                    <v-img :src="intAct.activity.imagePath" height="170px" width="270px" class="ma-3">
                                        <v-expand-transition>
                                            <div v-if="hover"
                                                class="d-flex transition-fast-in-fast-out darken-2 v-card--reveal text-h2 white--text"
                                                style="height: 100%;" >
                                                <v-btn @click="openH5PPlayer(intAct)" color="black" class="white--text"> 
                                                    <v-icon color="#ffb74d">mdi-play</v-icon>Play
                                                </v-btn>
                                            </div>
                                        </v-expand-transition>
                                    </v-img>
                                </v-col>
                            </v-hover> 
                             
                            <v-col >
                                <v-card-text>
                                    <h3 class="v-card__text__">
                                         {{intAct.activity.description}} 
                                    </h3>
                                </v-card-text>
                                <v-card-text>
                                    <router-link :to="{ name: 'BookDetail' , params: { unitSelectedFromStudent: intAct.activity.lesson.unit, bookId:classGroup.teacher.code.book.id }}">
                                        <p class="ma-0 pa-0">
                                            {{ $t('unit') }} {{intAct.activity.lesson.unit.number}}
                                        </p>
                                    </router-link>
                                    <router-link :to="{ name: 'BookDetail' , params: { lessonSelectedFromStudent:intAct.activity.lesson }}">
                                        <p class="ma-0 pa-0">
                                            {{ $t('lesson') }} {{intAct.activity.lesson.number}}:  {{intAct.activity.lesson.name}}
                                        </p>
                                    </router-link>
                                    <p v-if="intAct.homeworkDate">
                                        {{ $t('end_date') }}: {{intAct.homeworkDate}}
                                    </p>
                                    <p v-else>
                                        {{ $t('without_end_date') }}
                                    </p>
                                </v-card-text>
                            </v-col>
                            
                        </v-row>
                    </v-card>
                  </v-col>
                  
              </v-row>
            </v-container>
        </v-flex>
    </v-layout>
</template>

<script>
import i18n from '@/plugins/i18n';
import store from '../../common/store';
import RepositoryFactory from '../../repositories/RepositoryFactory';
const InteractionActivity = RepositoryFactory.get("interactionsActivities");
const ClassGroupsRepository = RepositoryFactory.get("classGroups");

  export default {
    name: 'PendingActivityList',

    data: () => ({
        interactionsActivities:[],
        search:'',
        showAlert: false,
        alertPendingSubscription:false,
        alertCancelledSubscription: false,
        classGroup:{}
    }),
 
    created(){
        this.setData(this.subscription)
    },

    methods:{
        async setData(){
                        
            const interactionsActivities_ = new Array()
            const activities_ = new Array()
            // const activitiesWithInteractionActivity_ = new Array()
            // console.log("teacher", teacherSubs)
            const classGroup = await ClassGroupsRepository.getById(this.$route.params.id)
            this.classGroup = classGroup
            await InteractionActivity.getPendingsByClassGroupIdAndUserId(this.$route.params.id,store.state.user.id).then(intActivities => {
                for(var i=0;i<intActivities.length;i++){
                    const interactionActivity_ = {
                        activity: new Object(),
                        id: Number,
                        subscription: new Object(),
                        teacherSubscription: new Object()
                    }
                    // console.log(intActivities)
                    interactionActivity_.activity.code = intActivities[i].activity.code
                    interactionActivity_.activity.description = intActivities[i].activity.description
                    interactionActivity_.activity.id = intActivities[i].activity.id
                    interactionActivity_.activity.idH5Pfile = intActivities[i].activity.idH5Pfile
                    interactionActivity_.activity.imagePath = 'http://localhost:8080/api/activities/images/'+intActivities[i].activity.imagePath
                    interactionActivity_.activity.lesson = intActivities[i].activity.lesson
                    interactionActivity_.activity.unit = intActivities[i].activity.lesson.unit
                    interactionActivity_.activity.students = intActivities[i].activity.students
                    interactionActivity_.activity.title = intActivities[i].activity.title
                    interactionActivity_.activity.typeH5P = intActivities[i].activity.typeH5P
                    interactionActivity_.activity.number = intActivities[i].activity.number
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
                if(interactionsActivities_.length == 0) this.showAlert = true
                
                
            })
            .catch(err => {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            });
        },
        async openH5PPlayer(item){
            // console.log("item", item)
            this.$router.push({
                name:'H5PPlayer',
                params:{ activityId:item.activity.id,idH5Pfile: item.activity.idH5Pfile,interactionId:item.id, activity:item.activity  }
            })
            
        }
    }
  }
</script>

<style scoped>

.v-card__text__{
    color: #000;;
    justify-content: center;
    /* font-family:  cursive; */
}
.v-card__text_ {
    color:#78909C;
    justify-content: center;
    /* font-family:  cursive; */
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
