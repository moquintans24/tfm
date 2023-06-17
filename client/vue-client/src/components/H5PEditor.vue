<template>
  <v-container>
    <v-card>
      <v-card-title class="v-card__title" v-if="activity">{{ $t('activity') }}: {{ activity.title }} - {{ activity.code }} </v-card-title>
      <v-card-text>
        <v-row justify="end" dense>
          <h3 class="font-weight-bold text-h2 v-card__text__  ml-12" v-if="isNew" >{{ $t('new') }} H5P </h3>
          <h3 class="font-weight-bold text-h2 v-card__text__  ml-12" v-if="!isNew" >{{ $t('edit') }} H5P: {{ this.idH5Pfile }}</h3>
          <v-spacer></v-spacer>
          <v-btn  color="#757575" class="white--text mr-2" @click="discard()">
            <v-icon class="mr-2">mdi-close-box-outline</v-icon>
              {{ $t("discard") }}
            </v-btn>
            <v-btn  color="success" class="white--text mr-12" @click="save()">
              <v-icon class="mr-2">save</v-icon>
              {{ $t("save") }}
            </v-btn>
        </v-row>
        <v-row justify="center" dense>
          <v-col cols="12" md="10">
            <div class="h5p">
              <h5p-editor ref="editor" />
            </div>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script lang="js">
import RepositoryFactory from "@/repositories/RepositoryFactory";
const ActivityRepository = RepositoryFactory.get("activities");

try {
  const H5PWebcomponents = require("@lumieducation/h5p-webcomponents");
  H5PWebcomponents.defineElements("h5p-editor");
} catch {}

export default {
  name: 'H5PEditor',
  data() {
    return{
      h5pEditor: null,
      activity: null,
      alert: false,
      loading: false,
      saving: false,
      saveError: false,
      saveErrorMessage: '',
      isNew:false,
      idH5Pfile:null
    }
  },
  async mounted(){
    // console.log("mounted")
    if(this.$route.params.activityId){
      const activity = await ActivityRepository.getById(this.$route.params.activityId);
      if(!activity.idH5Pfile) this.isNew = true
      this.activity = activity
    }
    if(this.$route.params.idH5Pfile) this.idH5Pfile = this.$route.params.idH5Pfile
  },
  async beforeRouteEnter(to,from,next){
    const activity = await ActivityRepository.getById(to.params.activityId);
    next(vm => vm.setData(activity))
  },
  methods:{
    async setData(activity){
      // console.log("setData",this.$route.params.activityId)
      
      this.loading = true;
      this.h5pEditor = this.$refs.editor;
      this.h5pEditor.addEventListener("editorloaded", () => {
          this.loading = false;
        }
      );
      this.activity = activity
      this.loadEditor();
      
    },
    async loadEditor(){
      this.h5pEditor.setAttribute("content-id", this.activity && this.activity.idH5Pfile ? this.activity.idH5Pfile : "new");

      this.h5pEditor.loadContentCallback = async (contentId)=> {
        // console.log("contentID",contentId)
        // this.$log.debug(
        //   `ContentService: Getting information to edit ${contentId}...`
        // );
        const res = await fetch('/h5p/'+ contentId +'/edit');
        if (!res || !res.ok) {
          throw new Error(`${res.status} ${res.statusText}`);
        }
        this.loading = false;
        return res.json();
      };
      this.h5pEditor.saveContentCallback = async (contentId, requestBody) => { 
        this.saving = true;

        let body = JSON.stringify(requestBody);
        // console.log("save content callback", body)
        const res = contentId 
          ? await fetch('/h5p/' + contentId, { //contentId es el identificador del fichero que tenemos asociado a una actividad
            method: 'PATCH',
            headers: {
              'Content-Type': 'application/json'
            },
            body
          })
          : await fetch('/h5p', { //Si no hay id es porque estamos creando una actividad nueva
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            },
            body
          });

        if (!res || !res.ok) {
          throw new Error(
            `${res.status} ${res.statusText} - ${await res.text()}`
          );
        }
        return res.json();
      };
    },
    async save(){
        const returnData = await this.h5pEditor.save();
        // console.log("save",returnData)
        // console.log(this.activity)
        if(returnData){
          this.idH5Pfile = returnData.contentId
          this.activity.idH5Pfile = returnData.contentId
          await ActivityRepository.update(this.activity).then((activity) => {
            this.$router.replace({
                name:'AddActivity',
                params: { bookId: this.$route.params.bookId,lessonId:this.$route.params.lessonId,activityId:activity.id,activity:activity }
            })
          })
        }
    },
    discard(){
      this.$router.go(-1);
    }
  }
};
</script>
<style scoped>
.h5p {
  width: 100%;
  align-items: center;
  justify-content: center;
}
.v-card__title {
    color:#22555A;
    font-weight: bold;
    justify-content: center;
  }
.v-card__text__ {
    color: #000;;
    justify-content: center;
    /* font-family:  cursive; */
}
</style>
