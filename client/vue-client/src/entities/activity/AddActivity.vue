<template>
    <v-layout align-center justify-center>
      <v-flex xs12 sm12 md10>
        <v-card raised >
            <v-card-title v-if="!editActivity" class="v-card__title"> {{ $t('add_activity') }}</v-card-title>
            <v-card-title v-else class="v-card__title">   {{ $t('edit_activity') }} </v-card-title>
            <v-container fluid >
            <v-card-actions >
              <v-btn v-if="activity.idH5Pfile"  color="#BA3200" class="white--text" @click="deleteH5PContent()">
                <v-icon class="mr-2">mdi-delete-forever</v-icon>{{ $t('delete') }} H5P</v-btn> 
                <v-spacer></v-spacer>
              <v-btn v-if="activity.idH5Pfile"  color="#43a047" class="white--text" @click="h5pPlay(activity)">
                <v-icon class="mr-2">mdi-play</v-icon>Play H5P</v-btn> 
              <v-btn v-if="activity.idH5Pfile"  color="#2196f3" class="white--text" @click="downloadH5P()">
                <v-icon class="mr-2">mdi-file-download-outline</v-icon>{{ $t('download') }} H5P</v-btn> 
              <v-btn v-if="activity.idH5Pfile"  color="#FFA726" class="white--text" @click="H5PEditorContent()">
                <v-icon class="mr-2">mdi-pencil</v-icon>{{ $t('edit') }} H5P</v-btn> 
              <v-btn v-if="activity.id && !activity.idH5Pfile"  color="#FFA726" class="white--text" @click="H5PEditorContent()">
                <v-icon class="mr-2">mdi-new-box</v-icon>{{ $t('create') }} H5P</v-btn> 
            </v-card-actions>
            <v-card-text>
                <v-form ref="form">
                    <v-row>
                        <v-col cols="12" sm="4" class="pb-0 pt-0">
                            <v-text-field 
                                v-model="activity.title"
                                :rules="required_field($t('required_title'))"
                                type="text"
                                required
                            ><template #label>
                                {{$t('title')}}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="4" class="pb-0 pt-0">
                            <v-text-field 
                                v-model="activity.code"
                                :rules="required_field($t('required_code'))"
                                type="text"
                                required
                            ><template #label>
                                {{$t('code')}}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="4" class="pb-0 pt-0">
                            <v-text-field 
                                v-model="activity.number"
                                :rules="required_field($t('required_number'))"
                                type="number"
                                required
                            ><template #label>
                                {{$t('number')}}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                        </v-col>
                        <v-col cols="12" class="pb-0 pt-0">
                            <v-textarea
                                v-model="activity.description"
                                counter
                                :rules="required_field($t('required_description'))"
                                required
                                maxlength="50"
                            ><template #label>
                                {{$t('description')}}<span class="red--text"><strong>* </strong></span>
                            </template></v-textarea>
                        </v-col>
                        <v-col cols="12" class="pb-0 pt-0">
                            <v-text-field 
                            v-model="book.title"
                            type="text"
                            readonly
                            :label="$t('book')"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" class="pb-0 pt-0">
                            <v-text-field 
                            v-model="book.course"
                            type="text"
                            readonly
                            :label="$t('course')"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" class="pb-0 pt-0">
                            <v-text-field 
                            v-model="book.level"
                            type="number"
                            readonly
                            :label="$t('level')"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" class="pb-0 pt-0">
                            <v-text-field 
                            v-model="unit.number"
                            type="number"
                            readonly
                            :label="$t('unit')"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="12" sm="6" class="pb-0 pt-0">
                            <v-text-field 
                            v-model="lesson.number"
                            type="number"
                            readonly
                            :label="$t('lesson')"
                            ></v-text-field>
                        </v-col>
                    </v-row>                    
                    <div v-if="!editActivity">
                        <span>{{$t('activity_image')}}</span>
                        <div v-if="!file" id="app"  @dragover.prevent @drop.prevent class="text-center"> 
                        <div :class="['dropZone', dragging ? 'dropZone-over' : '']" @dragenter="dragging = true" @dragleave="dragging = false"  @drop="handleFileDrop">
                            
                          <div class="dropZone-info" @drag="onChange">
                            <v-icon :class="[dragover ? 'mt-2, mb-6' : 'mt-5']" size="50">
                              mdi-cloud-upload
                            </v-icon>
                            <p>
                             {{$t('drop_file_click_select')}}
                            </p>
                            <p>{{$t('extension_support')}}: png,jpeg,jpg</p>
                          </div>
                          <input type="file" @change="onChange" required>
                          </div>
                        </div>
                        <div v-else class="dropZone-uploaded">
                          <div class="dropZone-uploaded-info">
                            <v-icon :class="[dragover ? 'mt-2, mb-6' : 'mt-5']" size="50">
                              mdi-cloud-check
                            </v-icon>
                            <span class="dropZone-title">Uploaded</span>
                            <v-list-item>
                              <v-list-item-content>
                                <v-list-item-title>
                                  {{ file.name }}
                                  <span class="ml-3 text--secondary">
                                    {{ file.size }} bytes</span
                                  >
                                </v-list-item-title>
                              </v-list-item-content>

                              <v-list-item-action>
                                <v-btn @click.stop="removeFile" icon>
                                  <v-icon> mdi-close-circle </v-icon>
                                </v-btn>
                              </v-list-item-action>
                            </v-list-item>
                          </div>
                        </div>
                    </div>
                    <div v-else style="margin:0 auto;">
                      
                      <v-row align="center">
                        <v-col cols="12" >
                            <v-img :src="image" width="260px" 
                                        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                                        height="180px">
                                <v-row class="fill-height ma-0" align="end" justify="center">
                                    <v-btn @click="onButtonClick" class="upload-btn"  dark small fab>
                                        <v-icon>mdi-camera </v-icon>
                                    </v-btn>
                                    <input ref="uploader" class="d-none" type="file" accept="image/*" @change="loadImage">
                                </v-row>
                            </v-img>
                        </v-col>
                    </v-row>  
                    </div>
                    
                </v-form>
            </v-card-text>
            <v-card-actions >
                <v-btn  color="#BA3200" class="white--text" @click="cancel()">{{ $t('cancel')}}</v-btn>
                <v-spacer></v-spacer>
                <v-btn  color="#1D8383" class="white--text" @click="accept()">{{ $t('ok')}}</v-btn>
            </v-card-actions>
          </v-container>
        </v-card>
      </v-flex>
    </v-layout>
</template>
  
<script>
import i18n from "@/plugins/i18n";
import RepositoryFactory from "../../repositories/RepositoryFactory";
const BooksRepository = RepositoryFactory.get("books");
const LessonsRepository = RepositoryFactory.get("lessons");
const ActivitiesRepository = RepositoryFactory.get("activities");
  
  export default {
    name: 'AddActivity',
    data() {
      return {
        activity:{},
        book:{},
        lesson:{},
        unit:{},
        file: '', /*imagen*/ 
        dragging: false,
        dragover: false,
        editActivity:false,
        image:null,
        formData:null
      };
    },
    async beforeRouteEnter(to,from,next){
        // console.log("params",to.params)
        const book = await BooksRepository.getById(to.params.bookId)
        const lesson = await LessonsRepository.getById(to.params.lessonId)
        if(to.params.activityId){
            const activity = await ActivitiesRepository.getById(to.params.activityId)
            next(vm => vm.setDataEditActivity(book,lesson,activity))
            return;
        }        
        next(vm => vm.setData(book,lesson))
    },
    methods: {
        setData(book,lesson){
            this.book = book
            this.lesson = lesson
            this.unit = lesson.unit
            
        },
        async setDataEditActivity(book,lesson,activity){

            this.book = book
            this.lesson = lesson
            this.unit = lesson.unit
            this.activity = activity
            this.activity.image  = 'http://localhost:8080/api/activities/images/'+activity.imagePath
            this.image = 'http://localhost:8080/api/activities/images/'+activity.imagePath
            this.editActivity = true
        },
        required_field(msg){
            return [value => !!value || msg]
        },
        validate () {
            this.$refs.form.validate()
        },
        async accept(){
            if (!this.$refs.form.validate()) {
                return;
            }
            if(this.$route.params.activityId){
                await ActivitiesRepository.update(this.activity).then(()=>{
                    this.$router.push({ name: "EditBook",params:{ id: this.book.id, book:this.book, editBook:true } });
                }).catch(err => {
                    this.$notify({
                        text: err.response.data.message,
                        type: "error"
                    });
                });
                if(this.formData){
                  this.updateImage_(this.formData)
                }
            }else{
                let formData = new FormData();
                if(this.file!='') formData.append('image', this.file);
                
                formData.append('activityCode',this.activity.code);
                formData.append('activityTitle',this.activity.title);
                formData.append('activityNumber',this.activity.number);
                formData.append('activityDescription',this.activity.description);
                formData.append('lessonId',this.lesson.id)
                await ActivitiesRepository.save(formData).then((activity)=>{
                    this.$router.replace({name: 'H5PEditor',params:{activityId:activity.id}})
                }).catch(err => {
                    this.$notify({
                        text: err.response.data.message,
                        type: "error"
                    });
                });
            }
            

            
        },
        cancel(){
                this.$router.go(-1);
        },
        onChange(e) {
            var files = e.target.files || e.dataTransfer.files;
            
            if (!files.length) {
                this.dragging = false;
                return;
            }
            
            this.createFile(files[0]);
        },

        handleFileDrop(e) {
            let droppedFiles = e.dataTransfer.files;
            const t = this.$t.bind(this)
            if(droppedFiles.length > 1){
                this.dragging = false;
                alert(t('only_one_file'));
                return;
            }
            if(!droppedFiles) {
                this.dragging = false;
                return;
            }
            this.createFile(droppedFiles[0]);
    
        },
        createFile(file) {
            const t = this.$t.bind(this)
            if (!file.type.match('image.*')) {
                alert(t('please_select_image_file'));
                this.dragging = false;
                return;
            }     
            if (file.size > 5000000) {
                alert('please check file size no over 5 MB.')
                this.dragging = false;
                return;
            }
            this.file = file;
            this.dragging = false;
        },
        removeFile() {
            this.file = '';
        },
        loadImage(event) {
            var input = event.target;
            if (input.files) {
                var reader = new FileReader();
                reader.onload = (e) => {
                  this.image = e.target.result
                }
                
                let formData = new FormData();

                formData.append('file', input.files[0]);
                formData.append('name', this.activity.code);
                formData.append('type', 'activity');
                formData.append('id', this.activity.id);

                this.formData = formData;
                // this.updateImage_(formData)
                
                reader.readAsDataURL(input.files[0]);
            }
        },
        async updateImage_(formData){
            await ActivitiesRepository.updateImage(formData);
        },
        onButtonClick() {
            this.isSelecting = true;
            window.addEventListener(
                "focus",
                () => {
                  this.isSelecting = false
                },
                { once: true }
            );
            this.$refs.uploader.click()
            
        },
        H5PEditorContent(){
          this.$router.push({name: 'H5PEditor',params:{activityId:this.activity.id, idH5Pfile:this.activity.idH5Pfile}})
        },
         downloadH5P(){
          const contentId = this.activity.idH5Pfile;
          const result =  fetch(`/h5p/download/${contentId}`);
          result.then(async (res) =>{
            const blob = await res.blob();
            const newBlob = new Blob([blob]);

            const blobUrl = window.URL.createObjectURL(newBlob);
            const extension = 'h5p'
            const link = document.createElement('a');
            link.href = blobUrl;
            link.setAttribute('download', `${this.activity.idH5Pfile}.${extension}`);
            document.body.appendChild(link);
            link.click();
            link.parentNode.removeChild(link);

            // clean up Url
            window.URL.revokeObjectURL(blobUrl);
          })
        },
        async deleteH5PContent(){
          const contentId = this.activity.idH5Pfile;
          const activities = await ActivitiesRepository.getByIdH5PFile(contentId);
          //Si tenemos más de una actividad asociada a ese id h5p, no podemos eliminar el fichero
          if(activities.length > 1){
            this.activity.idH5Pfile = null
            await ActivitiesRepository.update(this.activity).then((activity) => {
              this.activity = activity
            })
          }else{
            const result = await fetch(`/h5p/${contentId}`, {
              method: 'delete'
            });
            if (!result.ok) {
                throw new Error(
                    `Error while deleting content: ${result.status} ${
                        result.statusText
                    } ${await result.text()}`
                );
            }else{
              this.activity.idH5Pfile = null
              await ActivitiesRepository.update(this.activity).then((activity) => {
                this.activity = activity
              })
            }
          }
          
        },
        h5pPlay(activity){
            this.$router.push({
                name:'H5PPlayer',
                params:{ activityId:activity.id,idH5Pfile:activity.idH5Pfile, activity:activity  }
            })
        }

    }
  };
  </script>
  
  <style scoped>
  .v-card__title {
    color:#22555A;
    font-weight: bold;
    justify-content: center;
  }
  .upload-btn{
    position: absolute !important;
      z-index: 999;
      top: 121px;
      color: #EDFFFC;
      background: #22555A;
      background: #22555A;
      background: linear-gradient(50deg, #EDFFFC 0%, #69c4cc 72%);
  }
  
  .avatar-shadow{
  box-shadow: 0px 0px 10px 0px #22555A;
  -webkit-box-shadow: 0px 0px 10px 0px #22555A;
  -moz-box-shadow: 0px 0px 10px 0px #22555A;
  }
.dropZone {
  /* width: 80%; */
  min-height: 8rem;
  position: relative;
  border: 2px dashed #90a4ae;
}

.dropZone:hover {
  border: 2px solid #2e94c4;
}

.dropZone:hover .dropZone-title {
  color: #1975A0;
}

.dropZone-info {
  color: #A8A8A8;
  position: absolute;
  top: 50%;
  width: 100%;
  transform: translate(0, -50%);
  text-align: center;
}

.dropZone-title {
  color: #787878;
}

.dropZone input {
  position: absolute;
  cursor: pointer;
  top: 0px;
  right: 0;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
}

.dropZone-over {
  background: #5C5C5C;
  opacity: 0.8;
}

.dropZone-uploaded {
  min-height: 10rem;
  position: relative;
  border: 2px dashed #90a4ae;
}

.dropZone-uploaded-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #A8A8A8;
  position: absolute;
  top: 50%;
  width: 100%;
  transform: translate(0, -50%);
  text-align: center;
}



  </style>