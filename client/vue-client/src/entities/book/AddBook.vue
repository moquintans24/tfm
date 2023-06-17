<template>
    <v-layout align-center justify-center>
      <v-flex xs12 sm12 md10>
        <v-card raised >
            <v-card-title class="v-card__title"> {{ $t('add_book') }}</v-card-title>
            <v-container fluid >
                
            <v-card-text>
                <v-radio-group v-model="bookCode" row mandatory >
                    <template #label>
                      {{ $t('book_with_code') }}<span class="red--text"><strong>* </strong></span>
                    </template>
                    <v-tooltip top>
                        <template v-slot:activator="{ on, attrs }">
                            <v-radio label="Profesor/a" value="profesor" color="#22555A" v-bind="attrs" v-on="on"></v-radio>
                        </template>
                        <span>{{ $t('tooltip_to_book_code_teacher') }}</span>
                    </v-tooltip>
                    <v-tooltip top>
                        <template v-slot:activator="{ on, attrs }">
                            <v-radio label="Alumno/a" value="alumno" color="#22555A" v-bind="attrs" v-on="on"></v-radio>
                        </template>
                        <span>{{ $t('tooltip_to_book_code_student') }}</span>
                    </v-tooltip>
                    
                    
                </v-radio-group>
                <v-form ref="form" >
                    <v-text-field 
                        v-model="title"
                        :rules="required_field($t('required_title'))"
                        type="text"
                        required
                    ><template #label>
                        Título<span class="red--text"><strong>* </strong></span>
                    </template></v-text-field>
                    <v-text-field 
                        v-model="course"
                        :rules="required_field($t('required_course'))"
                        type="text"
                        required
                    ><template #label>
                        Curso<span class="red--text"><strong>* </strong></span>
                    </template></v-text-field>
                    <v-text-field 
                        v-model="level"
                        :rules="required_field($t('required_level'))"
                        type="number"
                        required
                    ><template #label>
                        Nivel<span class="red--text"><strong>* </strong></span>
                    </template></v-text-field> 
                    
                    <div class="text-center">
                        <div v-if="!file" id="app"  @dragover.prevent @drop.prevent> 
                        <div :class="['dropZone', dragging ? 'dropZone-over' : '']" @dragenter="dragging = true" @dragleave="dragging = false"  @drop="handleFileDrop">
                          <div class="dropZone-info" @drag="onChange">
                            <v-icon :class="[dragover ? 'mt-2, mb-6' : 'mt-5']" size="60">
                              mdi-cloud-upload
                            </v-icon>
                            <p>
                             {{$t('drop_file_click_select')}}
                            </p>
                            <div class="dropZone-upload-limit-info">
                              
                              <div>{{$t('extension_support')}}: png,jpeg,jpg</div>
                              <!-- <div>maximum file size: 5 MB</div> -->
                            </div>
                          </div>
                          <input type="file" @change="onChange" required>
                          </div>
                        </div>
                        <div v-else class="dropZone-uploaded">
                          <div class="dropZone-uploaded-info">
                            <v-icon :class="[dragover ? 'mt-2, mb-6' : 'mt-5']" size="60">
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
                    
                </v-form>
            </v-card-text>
            <v-card-actions >
                <v-btn  color="#BA3200" class="white--text" @click="cancel()">Cancelar</v-btn>
                <v-spacer></v-spacer>
                <v-btn  color="#1D8383" class="white--text" @click="accept()">Aceptar</v-btn>
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
  
  export default {
    name: 'AddBook',
    data() {
      return {
        title:'',
        course: null,
        level:'',
        description: '',
        bookCode:'profesor',
        loader:null,
        loading:false ,
        file: '',
        dragging: false,
        dragover: false
      };
    },
    watch: {
      loader () {
        const l = this.loader
        this[l] = !this[l]

        setTimeout(() => (this[l] = false), 3000)

        this.loader = null
      },
    },
    methods: {
      required_field(msg){
        return [value => !!value || msg]
      },
      validate () {
        this.$refs.form.validate()
      },
      async accept(){
        if (!this.$refs.form.validate() && this.file== '') {
          return;
        }
        
        let formData = new FormData();

        formData.append('file', this.file);
      
        formData.append('bookTitle',this.title);
        formData.append('bookCourse',this.course);
        formData.append('bookLevel',this.level);

  
        await BooksRepository.save(formData).then(() => {
          this.$router.push({ name: "BookList" });
        }).catch(err => {
            this.$notify({
                text: err.response.data.message,
                type: "error"
            });
        });
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
        this.file = file;
        this.dragging = false;
      },
      removeFile() {
        this.file = '';
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
  min-height: 10rem;
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

.dropZone-upload-limit-info {
  display: flex;
  justify-content: flex-start;
  flex-direction: column;
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

.removeFile {
  width: 200px;
}
  </style>