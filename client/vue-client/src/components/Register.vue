<template>
    <!-- <v-app> -->
    <v-content>
      <v-layout align-end justify-end>
        <div  style="justify-content: flex-end">
          <v-menu offset-y origin="center center" transition="scale-transition">
              <template v-slot:activator="{ on, attrs }">
                <v-btn color="white" class="ma-2 white--text" fab v-bind="attrs" v-on="on">
                  <span>
                    <v-img
                    :src="getLanguageImage"
                    class="my-3" contain width="35" height="20" 
                  ></v-img>
                  </span>
                </v-btn>
              </template>
            <v-list>
              <v-list-item v-for="(item, index) in items" :key="index" @click="changeLanguage(index,item)">
              <v-list-item-avatar tile size="30">
                      <v-img :src="item.image" class="my-3" contain width="35" height="20"></v-img>
                    </v-list-item-avatar>
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </div>
      </v-layout>
      <v-layout align-center justify-center>
        <v-flex xs12 sm12 md7 >
          <v-container fluid>
            <v-card raised>
            <v-card-title class="v-card__title"> {{ $t('register') }}</v-card-title>
              <v-card-text>
                  <v-form ref="form"  v-model="valid" lazy-validation>
                    <v-text-field 
                      v-model="code"
                      :rules="required_field($t('required_code'))"
                      type="text"
                      required
                    ><template #label>
                      {{ $t('code') }}<span class="red--text"><strong>* </strong></span>
                    </template></v-text-field>
                    <v-text-field 
                      v-model="name"
                      :rules="required_field($t('required_name'))"
                      prepend-icon="notes"
                      type="text"
                      required
                    ><template #label>
                        {{ $t('name') }}<span class="red--text"><strong>* </strong></span>
                    </template></v-text-field>
                    <v-text-field 
                      v-model="last_name"
                      :rules="required_field($t('required_last_name'))"
                      prepend-icon="notes"
                      type="text"
                      required
                    ><template #label>
                        {{ $t('last_name') }}<span class="red--text"><strong>* </strong></span>
                    </template></v-text-field>
                    <v-text-field 
                        v-model="date"
                        :rules="required_field($t('required_birthdate'))"
                        name="birthdate"
                        prepend-icon="mdi-calendar"
                        type="date"
                        required
                    ><template #label>
                        {{$t('birthdate')}}<span class="red--text"><strong>* </strong></span>
                    </template></v-text-field>
                    <v-select
                      v-model="selected_language"
                      :items="items"
                      prepend-icon="language"
                      :rules="required_field($t('required_language'))"
                      item-text="language"
                    ><template #label>
                      {{ $t('language') }}<span class="red--text"><strong>* </strong></span>
                      </template>
                      <template v-slot:selection="{ item, index }">
                          <img style="width: 35px" :src="item.image" class="mr-2 d-block">{{ item.name }}
                      </template>
                      <template v-slot:item="slotProps" >
                        <img style="width: 35px" :src="slotProps.item.image" class="mr-2 d-block"/>
                        
                        {{slotProps.item.name}}
                      </template>
                    </v-select>
                    <v-text-field 
                      v-model="school"
                      prepend-icon="school"
                      type="text"
                      label="Centro"
                      required></v-text-field>
                    <v-text-field 
                      v-model="email"
                      :rules="required_email($t('required_email'))"
                      prepend-icon="mail"
                      name="email"
                      type="text"
                      required
                    ><template #label>
                      Email<span class="red--text"><strong>* </strong></span>
                    </template></v-text-field>
                    <v-text-field 
                      v-model="login"
                      :rules="required_field($t('required_login'))"
                      prepend-icon="mdi-account"
                      name="login"
                      type="text"
                      required
                    ><template #label>
                      Login<span class="red--text"><strong>* </strong></span>
                    </template></v-text-field>
                    <v-text-field
                        v-model="password" 
                        prepend-icon="vpn_key"
                        :append-icon="show_passwd ? 'mdi-eye' : 'mdi-eye-off'"
                        :type="show_passwd ? 'text' : 'password'"
                        name="password"
                        @click:append="show_passwd = !show_passwd"
                        :rules="required_field($t('required_passwd'))"
                        required
                    >   <template #label>
                        {{ $t('passwd') }}<span class="red--text"><strong>* </strong></span>
                        </template>
                    </v-text-field>
                    <v-text-field
                        v-model="confirm_password" 
                        prepend-icon="vpn_key"
                        :append-icon="show_passwd2 ? 'mdi-eye' : 'mdi-eye-off'"
                        :type="show_passwd2 ? 'text' : 'password'"
                        name="password"
                        @click:append="show_passwd2 = !show_passwd2"
                        :rules="required_passwd($t('required_passwd'))"
                        required
                    >   <template #label>
                        {{ $t('repeat_passwd') }}<span class="red--text"><strong>* </strong></span>
                        </template>
                    </v-text-field>
                    <v-radio-group v-model="rol" row mandatory>
                        <template #label>
                          {{ $t('role') }}<span class="red--text"><strong>* </strong></span>
                        </template>
                        <v-radio :label="$t('teacher')" value="rol_profesor" color="#22555A"></v-radio>
                        <v-radio :label="$t('student')" value="rol_alumno" color="#22555A" ></v-radio>
                    </v-radio-group>
                </v-form>
              </v-card-text>
              <v-card-actions class="justify-center">
                
                <v-slide-x-reverse-transition>
                  <v-tooltip 
                    v-if="formHasErrors"
                    top
                  >
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        icon
                        class="my-0"
                        v-bind="attrs"
                        @click="reset"
                        v-on="on"
                      >
                        <v-icon>mdi-refresh</v-icon>
                      </v-btn>
                    </template>
                    <span>Restablecer formulario</span>
                  </v-tooltip>
                </v-slide-x-reverse-transition>  
                <v-btn color="#1D8383" class="white--text" @click="register()">{{ $t('register') }}</v-btn>
              </v-card-actions>
            </v-card>
          </v-container>
        </v-flex>
      </v-layout>
    </v-content>
    <!-- </v-app> -->
</template>
  
  
<script>
import auth from "../common/auth";
import i18n from "@/plugins/i18n";
import RepositoryFactory from "@/repositories/RepositoryFactory";
const UsersRepository = RepositoryFactory.get("users");

export default {
  name: 'Register',
  data() {
    return {
      valid: true,
      code:'',
      email: '',
      show_passwd:false,
      password: '',
      confirm_password: '',
      show_passwd2:false,
      school: '',
      rol:'rol_profesor',
      date: '',
      menu:'',
      selected_language:'',
      name:'',
      last_name:'',
      login:'',
      items: [
                {
                  language: 'es-ES',
                  name: 'Español',
                  image: require('../assets/Flag_of_Spain.svg')
                },
                {
                  language: 'gl-ES',
                  name: "Galego",
                  image: require('../assets/Flag_of_Galicia.svg')
                },
                {
                  language: 'en-GB',
                  name: "Inglés",
                  image: require('../assets/Flag_of_the_United_Kingdom.svg')
                }
              ],
      formHasErrors: false                        
    };
  },
  computed:{
    getLanguageImage(){
      if (i18n.locale === 'es-ES') {
        return require('../assets/Flag_of_Spain.svg')
        // return Promise.resolve(setI18nLanguage(lang))
      }else if(i18n.locale === 'gl-ES'){
        return require('../assets/Flag_of_Galicia.svg')
      }else{
        return require('../assets/Flag_of_the_United_Kingdom.svg')
      }
    }
  },
  methods: {
    changeLanguage(id,item) {
      // console.log("change",id,item, item.name)
      this.reset()
      this.resetValidation()
      i18n.locale = item.language
      return this.language = item.image
    },
    reset () {
        this.$refs.form.reset()
        this.formHasErrors = false
        this.rol='rol_profesor'
        
      },
    resetValidation () {
        this.$refs.form.resetValidation()
    },
    required_field(msg){
      return [value => !!value || msg]
    },
    required_email(msg){
      const t = this.$t.bind(this)
        return [ v => !!v || msg,
                    v => /.+@.+\..+/.test(v) || t('valid_email')]
    },
    required_passwd(msg){
      const t = this.$t.bind(this)
        return [ v => !!v || msg,
         this.password === this.confirm_password || t('password_match')]
    },
    async register(){
      if(!this.$refs.form.validate()) {
        this.formHasErrors = true
        return;
      }
      const user = new Object()
      user.code = this.code
      user.name = this.name
      user.lastName = this.last_name
      user.language = this.selected_language
      user.email = this.email
      user.center = this.school
      user.birthDate = this.date
      user.login = this.login
      user.password = this.password
      if(this.rol == 'rol_profesor'){
        user.rol = "Profesorado"
      }else{
        user.rol = "Alumnado"
      }
      
      // console.log(user)
      const register = new Object()
      register.user = user
      register.code = this.code
      await UsersRepository.registerUser(register).then(() => {
            auth
            .login({
              login: this.login,
              password: this.password
            })
            .then(() => {
              i18n.locale = this.selected_language
              
              this.$router.replace({name: 'Home'})
            })
            .catch(err => {
              this.$notify({
                text: err.response.data.message,
                type: "error"
              });
            });
        })
        .catch(err => {
          this.$notify({
            text: err.response.data.message,
            type: "error"
          });
        });
    }
  }
};
</script>
  
<style scoped>
.v-card__title {
  color:#1D8383;
  justify-content: center;
}
</style>
  
  
  
  