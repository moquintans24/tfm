<template>
    <!-- <v-app> -->
        <v-content>
            <v-layout align-center justify-center>
                <v-flex xs12 sm12 md6>
                    <v-container fluid>
                        <v-card class="mt-12" v-if="showAlert">
                            
                            <div class="text-center">
                                <v-icon class="pt-10" size="60">
                                    mdi-email-check-outline
                                </v-icon>
                                <v-card-title class="v-card__title"> {{ $t('email_sent') }}</v-card-title>
                            </div>
                            <v-card-text class="text-center">
                                {{ $t('text_check_email') }}
                            </v-card-text>
                            <v-card-actions class="justify-center">
                                <v-btn  color="#1D8383" class="white--text mb-10" @click="accept()">{{ $t('ok') }}</v-btn>
                            </v-card-actions>
                        </v-card>
                        <v-card class="mt-12" v-else>
                            
                            <div class="text-center">
                                <v-icon class="pt-10" size="60">
                                    mdi-lock-reset
                                </v-icon>
                                <v-card-title class="v-card__title"> {{ $t('forgotten_password') }}</v-card-title>
                            </div>
                            <v-card-text class="text-center">
                                {{ $t('reset_password') }}
                            </v-card-text>
                            <v-card-text>
                                <v-form ref="form" class="ml-10 mr-10 mt-0">
                                    <v-text-field 
                                        v-model="email_login"
                                        :rules="required_field($t('required_email_login'))"
                                        prepend-icon="mail"
                                        type="text"
                                        required
                                    ><template #label>
                                        Email/Login<span class="red--text"><strong>* </strong></span>
                                    </template></v-text-field>
                                </v-form>
                            </v-card-text>
                            <v-card-actions class="justify-center">
                                <v-btn  :loading="loading" :disabled="loading" color="#1D8383" class="white--text mb-10" @click="forgotPassword_()">{{ $t('send') }}</v-btn>
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
    name: 'ForgotPassword',
    data() {
      return {
        email_login: '',  
        showAlert: '',
        loading:false  
      };
    },
    methods: {
        forgotPassword_() {
            if(!this.$refs.form.validate()){
                return;
            } 
            this.loading = true
            auth.forgotPassword(this.email_login)
            .then(() => {
                this.showAlert = true
                this.loading = false
            })
            .catch(err => {
                this.$notify({
                text: err.response.data.message,
                type: "error"
                });
            });
        },
        accept(){
            this.$router.replace({
                name:'Login'
            })
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
  
  
  
  