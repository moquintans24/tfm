<template>
  <v-layout align-center justify-center>
    <v-flex xs12 sm12 md10>
    
      <v-container fluid >
        <v-card raised color="#fafafa">
          <v-card-title  class="v-card__title"
                    style="background: #DDEEEB; background: linear-gradient(to right, #DDEEEB 0%,#CFE7FA 50%, #22555A 100%);" height="92px"> 
                    {{ $t('profile') }} 
          </v-card-title>
          <v-row justify="center">
            <v-col align-self="start" class="d-flex justify-center align-center pa-0 mt-4" cols="12">
                <v-avatar class="profile avatar-center-heigth avatar-shadow" color="#DDEEEB" size="164">
                  <v-btn v-if="userAuthenticatedId == user.id" @click="onButtonClick" class="upload-btn" large icon>
                    <v-icon>
                      mdi-camera
                    </v-icon>
                  </v-btn>
                  <input ref="uploader" class="d-none" type="file" accept="image/*" @change="loadImage">
                  <v-img v-if="user.linkImage != 'default_user_image.jpg'" :src="usrImage"></v-img>
                  <!-- Si no hay imagen poner las iniciales -->
                  <h1 v-if="user.linkImage == 'default_user_image.jpg'" class="user_initials">{{ user.initials }}</h1>
                </v-avatar>
              </v-col>
          </v-row>
          <v-row class="pa-6" justify="center">
            <v-col cols="12" sm="10" > 
              <v-card-text class="pa-0 justify-center align-center">
                <v-card  elevation='24' color="white" width='auto' class='pa-6' >
                  <v-card-actions class="mt-n12">
                    <v-spacer></v-spacer>
                    <v-btn v-if="isDisabled && userAuthenticatedId == user.id" color="#22555A" fab small dark outlined @click="edit"  >
                      <v-icon>{{ formIcon }}</v-icon>
                    </v-btn>
                    <v-btn v-if="!isDisabled" color="#22555A" fab small dark outlined @click="saveProfile" >
                      <v-icon>{{ formIcon }}</v-icon>
                    </v-btn>
                    <v-btn v-if="!isDisabled" color="red" fab small dark outlined @click="cancel" >
                      <v-icon>mdi-close</v-icon>
                    </v-btn>
                  </v-card-actions>
                    <v-form ref="form" v-model="valid" lazy-validation >
                      <v-row>
                         <v-col cols='6'>
                            <div style="text-align:left" > {{ $t('name') }} </div>
                            <div style="text-align:left" > 
                              <v-text-field v-model="user.name" :disabled="isDisabled" 
                                :readonly="isReadonly" :rules="required_field($t('required_name'))"
                              ></v-text-field>
                            </div>
                          </v-col>
                        <v-col cols='6'>
                            <div style="text-align:left" > {{ $t('last_name') }} </div>
                            <div style="text-align:left" > 
                              <v-text-field v-model="user.lastName" :disabled="isDisabled" 
                                :readonly="isReadonly" :rules="required_field($t('required_last_name'))"
                              ></v-text-field>
                            </div>
                          </v-col>
                      </v-row>
                      <div style="text-align:left" > {{ $t('birthdate') }} </div>
                      <div style="text-align:left" > 
                        <v-text-field v-model="user.birthDate" type="date" :disabled="isDisabled"
                          :readonly="isReadonly" :rules="required_field($t('required_birthdate'))"
                        ></v-text-field>
                      </div>
                      <v-row>
                        <v-col cols="12">
                          <div style="text-align:left" > Email </div>
                          <v-text-field v-model="user.email"
                            :disabled="isDisabled"
                            :readonly="isReadonly" :rules="required_field($t('required_email'))"
                          ></v-text-field>
                        </v-col>
                      </v-row>
                      <div style="text-align:left" v-if="!isEditor && !isAdmin"> {{ $t('center') }} </div>
                      <div style="text-align:left" v-if="!isEditor && !isAdmin"> 
                        <v-text-field v-model="user.center" :disabled="isDisabled"
                        :readonly="isReadonly"
                        ></v-text-field>
                      </div>
                      <div style="text-align:left" > {{ $t('language') }} </div>
                      <div style="text-align:left" > 
                        <v-select
                          v-model="selected_language"
                          :items="items"
                          :rules="required_field($t('required_language'))"
                          item-text="language" :readonly="isReadonly" :disabled="isDisabled" @input="changeLanguage()">
                          <template v-slot:selection="{ item, index }">
                              <img style="width: 35px" :src="item.image" class="mr-2 d-block">{{ item.name }}
                          </template>
                          <template v-slot:item="slotProps" >
                            <img style="width: 35px" :src="slotProps.item.image" class="mr-2 d-block"/>
                              {{slotProps.item.name}}
                          </template>
                        </v-select>
                      </div>
                    </v-form>             
                </v-card>          
              </v-card-text>
            </v-col>
          </v-row>
        </v-card>
      </v-container>
    </v-flex>
  </v-layout>
</template>

<script>
import store from "../../common/store";
import RepositoryFactory from "@/repositories/RepositoryFactory";
import i18n from '@/plugins/i18n';
import auth from "../../common/auth";
const UsersRepository = RepositoryFactory.get("users");

export default {
  name: 'UserProfile',
  data() {
    return {
      user: {},
      usuario: {},
      password:'',
      show_passwd:'',
      formIcon:'mdi-pencil',
      editForm:false,
      isReadonly:true,
      isDisabled:true,
      valid: true,
      image:null,
      usrImage:'',
      userAuthenticatedId:store.state.user.id,
      selected_language:'',
      items: [
                {
                  language: 'es-ES',
                  name: 'Español',
                  image: require('../../assets/Flag_of_Spain.svg')
                },
                {
                  language: 'gl-ES',
                  name: "Galego",
                  image: require('../../assets/Flag_of_Galicia.svg')
                },
                {
                  language: 'en-GB',
                  name: "Inglés",
                  image: require('../../assets/Flag_of_the_United_Kingdom.svg')
                }
              ]  
    }
      
  },
  computed:{
    getLanguageImage(){
      if (i18n.locale === 'es-ES') {
        return require('../../assets/Flag_of_Spain.svg')
        // return Promise.resolve(setI18nLanguage(lang))
      }else if(i18n.locale === 'gl-ES'){
        return require('../../assets/Flag_of_Galicia.svg')
      }else{
        return require('../../assets/Flag_of_the_United_Kingdom.svg')
      }
    },
    isEditor() {
      return auth.isEditor();
    },
    isAdmin() {
      return auth.isAdmin();
    }
  },
  async beforeRouteEnter(to, from, next) {
      next(vm => vm.setData(to.params.id));
  },
  methods:{
    async setData(userId){
      try {
                const user = await UsersRepository.getById(userId)
                // console.log("user!!!",user)
                if(user != null){
                    this.user = user
                    this.user.initials = user.name.charAt(0).concat(user.lastName.charAt(0))
                    this.selected_language = user.language
                    if(!user.linkImage.includes("default_user_image")){
                      this.user.image = this.getRouteImage(user.linkImage)
                      this.usrImage = this.user.image 
                    }else{
                      this.user.initials = user.name.charAt(0).concat(user.lastName.charAt(0))
                    }
                }
                // console.log(this.user)
             
            } catch (err) {
                this.$notify({
                    text: err.response.data.message,
                    type:"error"
                })
            } 
    },
    getRouteImage(linkImage){
            return 'http://localhost:8080/api/users/images/'+linkImage
        },
    required_field(msg){
        return [value => !!value || msg]
    },
    loadImage(event) {
      var input = event.target;
      if (input.files) {
        var reader = new FileReader();
        reader.onload = (e) => {
          this.usrImage = e.target.result
        }
        this.image=input.files[0];
        
        let formData = new FormData();

        formData.append('file', input.files[0]);
        formData.append('name', this.user.login);
        formData.append('type', 'usuario');
        formData.append('id', this.user.id);

        this.updateImage_(formData)
        
        reader.readAsDataURL(input.files[0]);
      }
    },
    async updateImage_(formData){
      const img = await UsersRepository.updateImage(formData)
      this.setData(this.user.id)
      const user_ = await UsersRepository.updateUser(this.user,this.$route.params.id)
      auth.reloadUserData(user_)
        if(img!=null){
          this.user.image = this.getRouteImage(img)     
        } 
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
    onFileChanged(e) {
      this.selectedFile = e.target.files[0];
    },
    validate () {
      this.$refs.form.validate()
    },
    reset () {
      this.$refs.form.reset()
    },
    resetValidation () {
      this.$refs.form.resetValidation()
    },
    edit(){
      this.editForm = !this.editForm
      this.formIcon = 'mdi-content-save'
      this.isReadonly = !this.isReadonly
      this.isDisabled = !this.isDisabled
    },
    changeLanguage(){
      i18n.locale = this.selected_language
    },
    async saveProfile() {
      if(this.$refs.form.validate()){
        this.editForm = !this.editForm;
        this.formIcon = 'mdi-pencil'
        this.isReadonly = !this.isReadonly
        this.isDisabled = !this.isDisabled
        this.user.language = this.selected_language
        const user_ = await UsersRepository.updateUser(this.user,this.$route.params.id)
        auth.reloadUserData(user_)
      }
    },
    cancel(){
        this.editForm = !this.editForm;
        this.formIcon = 'mdi-pencil'
        this.isReadonly = !this.isReadonly
        this.isDisabled = !this.isDisabled
        i18n.locale = store.state.user.language
        this.setData(this.$route.params.id)
    },
    back(){
        this.$router.go(-1);
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
.user_initials{
    color:#22555A;
    font-weight: bold;
    justify-content: center;
    /* font-family:  cursive; */
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
</style>