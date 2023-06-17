<template>
    <v-container>
        <v-card >
            <br>
            <v-progress-linear v-if="loading" color="primary" indeterminate reverse ></v-progress-linear>
            <v-progress-linear v-if="error" color="red" value="100" ></v-progress-linear>
            <v-card-text class="black--text text-center" v-if="loading || error">
                <div v-if="loading">
                    {{ $t('loading_activity') }}
                </div>
                <div v-if="error">
                    {{ $t('activity_not_found') }}
                </div>
            </v-card-text>
            <div class="h5p mx-10" ref="playerWrapper"> <!-- pintamos el editor de h5p -->
                
            </div>
            <v-card-actions v-if="completedActivity">
                <v-spacer></v-spacer>
                <v-btn  color="#22555A" outlined class="ma-2"  @click="saveResult()">
                    <v-icon class="mr-2">save</v-icon>
                    {{ $t('finish') }}</v-btn
                >
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script>
import auth from "../common/auth";
import store from "../common/store";
import RepositoryFactory from "../repositories/RepositoryFactory";
const InteractionActivityRepository = RepositoryFactory.get("interactionsActivities");


try {
  const H5PWebcomponents = require("@lumieducation/h5p-webcomponents");
  H5PWebcomponents.defineElements("h5p-player");
} catch {}

export default {
    name: 'H5PPlayer',
    data() {
        return{
            loading:true,
            error:false,
            componenetDefined:false,
            saved:false,
            activity:{},
            completedActivity:false,
            exercise:null,
            activityStartTime:null
        }
    },
    beforeRouteEnter(to,from,next){
        // console.log(to.params)
        next(vm => vm.setData())
    },
    methods:{
        async setData(){
            try {
                this.exercise = this.$route.params.idH5Pfile
                const res = await fetch("/h5p/" + this.exercise + "/play");
                if (!res || !res.ok) {
                    this.error = true;
                    this.$emit("error", res);
                    return;
                }

                const h5pPlayer = document.createElement("h5p-player");

                this.$refs.playerWrapper.appendChild(h5pPlayer); //Añade el h5p player a la aplicación

                h5pPlayer.addEventListener("initialized", () => {
                    this.$emit("loaded");
                    // console.log("h5p pbject",h5pPlayer.h5pObject)
                    h5pPlayer.h5pObject.externalDispatcher.on("xAPI", (event) =>{
                        this.processStatement(event)
                    }
                    );
                });
                h5pPlayer.setAttribute("content-id", this.exercise);
                h5pPlayer.loadContentCallback = async (contentId) => {
                    return res.json();
                };
            } catch (err) {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            } finally {
            this.loading = false;
            }  
        },
        processStatement(event){
            let statement = event.data.statement;
            // console.log("processStatement",event)
            if(statement.verb.display["en-US"] === "answered" || statement.verb.display["en-US"] === "completed"){
                this.completedActivity = true
                // this.activity.score = event.getScore();
                // this.activity.maxScore = event.getMaxScore();
                this.activity.score = (event.getScore()*10)/event.getMaxScore();
                // console.log(event.getDuration())
                if(statement.result){
                    if(statement.result.duration) {
                        this.activity.duration = statement.result.duration.replace(/[^\d.-]/g, '');
                    }else{
                        this.activity.duration = Math.round((Date.now() - this.activityStartTime ) / 10) / 100
                    }
                }
                
            }
            if(statement.verb.display["en-US"] === "interacted" ){
                this.activityStartTime = Date.now() //Para, en la actividad find the word, poder obtener la duración
                // console.log("activity start time", this.activityStartTime)
            }
        },
        async saveResult(){
            // console.log("activity.duration", this.activity)
            if(auth.isStudent()){
                if(this.$route.params.interactionId){
                    await InteractionActivityRepository.saveDurationAndScore(this.$route.params.interactionId,this.activity.duration,this.activity.score).then(() => {
                        this.$router.go(-1);
                    })
                }else{
                    const interactionActivity = await InteractionActivityRepository.getByActivityIdAndUserId(this.$route.params.activityId, store.state.user.id);
                    if(interactionActivity){
                            await InteractionActivityRepository.saveDurationAndScore(interactionActivity.id, this.activity.duration,this.activity.score).then(() => {
                            this.$router.go(-1);
                        })
                    }  
                }
            }else{
                this.$router.go(-1);
            }
        }
    }
};
</script>
<style scoped lang="css">
.h5p {
  align-items: center;
  justify-content: center;
  height: 100%;
  width: auto;
  margin:auto;
}

.text-center {
  text-align: center;
  height: 100%;
  /* padding-top: 200px; */
}

::v-deep .h5p-joubelui-button {
  color: white;
  text-decoration: none;
}
</style>
