<template>
    <!-- <v-app> -->
        <v-content>
            <v-layout align-center justify-center>
                <v-flex xs12 sm12 md6>
                    <v-container fluid>
                        <v-card class="mt-12" v-if="showAlert">
                            
                            <div class="text-center">
                                <v-icon class="pt-10" size="60">
                                    mdi-check-circle-outline
                                </v-icon>
                                <v-card-title class="v-card__title"> {{ $t('login_password_established') }}</v-card-title>
                            </div>
                            <v-card-actions class="justify-center">
                                <v-btn  color="#1D8383" class="white--text mb-10" @click="accept()">{{ $t('ok') }}</v-btn>
                            </v-card-actions>
                        </v-card>
                        <v-card class="mt-12" v-else>
                            
                            <div class="text-center">
                                <v-icon class="pt-10" size="60">
                                    mdi-lock-reset
                                </v-icon>
                                <v-card-title class="v-card__title"> {{ $t('establish_login_password') }}</v-card-title>
                            </div>
                            <v-card-text class="text-center">
                                {{ $t('text_establish_login_password') }}
                            </v-card-text>
                            <v-card-text>
                                <v-form ref="form" class="ml-10 mr-10 mt-0">
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
                                </v-form>
                            </v-card-text>
                            <v-card-actions class="justify-center">
                                <v-btn :loading="loading" :disabled="loading" color="#1D8383" class="white--text mb-10" @click="saveLoginPassword()">{{ $t('send') }}</v-btn>
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
 
export default {
    name: 'NewLoginPassword',
    data() {
      return {
        showAlert:false,
        show_passwd:false,
        password: '',
        confirm_password: '',
        show_passwd2:false,  
        loading:false ,
        login:'' 
      };
    },
    methods: {
        saveLoginPassword() {
            if(!this.$refs.form.validate()){
                return;
            }  
            auth.saveLoginPassword(this.$route.params.id,this.login,this.password,this.$route.params.validationString)
            .then(() => {
                this.loading = false
                this.showAlert = true
            })
            .catch(err => {
                this.$notify({
                text: err.response.data.message,
                type: "error"
                });
            });
            
        },
        required_field(msg){
            return [value => !!value || msg]
        },
        required_passwd(msg){
            const t = this.$t.bind(this)
            return [ v => !!v || msg,
            this.password === this.confirm_password || t('password_match')]
        },
        accept(){
            this.$router.replace({
                name:'Login'
            })
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
  
  
  
  