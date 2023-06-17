<template>
  <!-- <v-app> -->
     <v-content>
        <div  style="display: flex; justify-content: flex-end">
          <v-menu offset-y origin="center center" transition="scale-transition">
              <template v-slot:activator="{ on, attrs }">
                <v-btn color="white" class="mb-2 white--text" fab v-bind="attrs" v-on="on">
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
        <v-content>
           <v-layout align-center justify-center>
              <v-flex xs12 sm12 md6>
                <v-container fluid>
                 <v-card>
                  <div class="text-center">
                                <!-- <v-img :src="require('../assets/digital_content.png')"></v-img> -->
                                <v-card-title class="v-card__title"> {{ $t('login') }}</v-card-title>
                            </div>
                  <!-- <v-card-title class="v-card__title"> {{ $t('login') }}</v-card-title> -->
                  
                    <v-card-text>
                       <v-form ref="form" >
                          <v-text-field 
                            v-model="email"
                            :rules="required_field($t('required_login'))"
                            prepend-icon="mail"
                            name="email"
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
                          ><template #label>
                            {{ $t('passwd') }}<span class="red--text"><strong>* </strong></span>
                          </template>
                      </v-text-field>
                      </v-form>
                    </v-card-text>
                    <v-card-actions class="justify-center">
                       <v-btn  color="#1D8383" class="white--text" @click="userLogin()">{{ $t('login') }}</v-btn>
                    </v-card-actions>
                    <v-card-text>
                     <div class="text-center clear">
                        <div class="link1">
                           <router-link :to="{ name: 'ForgotPassword' }">
                            {{ $t('forgot_passwd') }}
                           </router-link>
                        </div>
                        <div class="link2">
                           <router-link :to="{ name: 'Register' }">
                            {{ $t('create_account') }}
                           </router-link>
                        </div>
                      </div>
                    </v-card-text>
                 </v-card>
                </v-container>
              </v-flex>
           </v-layout>
        </v-content>
      </v-content>
  <!-- </v-app> -->
</template>


<script>
import auth from "../common/auth";
import store from "../common/store";
import i18n from '@/plugins/i18n';

export default {
  name: 'Login',
  data() {
    return {
      email: '',
      show_passwd:false,
      password: '',
      rules_email: {
        required: value => !!value || '' 
      },
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
              ]             
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
    userLogin() {
      if(!this.$refs.form.validate()){
        return;
      }    
      auth
        .login({
          login: this.email,
          password: this.password
        })
        .then(() => {
          if(localStorage.getItem("path_add_book_code")) this.$router.replace({name: 'BookList',params:{code:localStorage.getItem("path_add_book_code")}})
          else this.$router.replace({name: 'Home'})
        })
        .catch(err => {
          this.$notify({
            text: err.response.data.message,
            type: "error"
          });
        });  
      
    },
    changeLanguage(id,item) {
      // console.log("change",id,item, item.name)
      this.reset()
      this.resetValidation()
      i18n.locale = item.language
      return this.language = item.image
    },
    reset () {
        this.$refs.form.reset()
      },
    resetValidation () {
        this.$refs.form.resetValidation()
    },
    required_field(msg){
      return [value => !!value || msg]
    }
  }
};
</script>

<style scoped>
.v-card__title {
  color:#1D8383;
  justify-content: center;
}

.link1 {
  display: inline-block;

}
.link2 {
  display: inline-block;
  margin: 0 50px;
}

</style>



