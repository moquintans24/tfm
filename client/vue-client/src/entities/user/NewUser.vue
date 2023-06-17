<template>
    <div class="text-center">
        <v-dialog
            v-model="dialog"
            max-width="600px"
            persistent
            >
            <v-card >
                <v-card-title class="v-card__title_dialog"> {{ $t('add') }} {{ $t('content_editor') }}
                <v-spacer></v-spacer>
                <v-btn icon  @click="close">
                    <v-icon>mdi-close</v-icon>
                </v-btn>
                </v-card-title>
                <v-container>
                    <v-card-text>
                    <v-row>
                        <v-col>
                            <v-form ref="form" v-model="valid" lazy-validation>
                            <v-text-field 
                                v-model="user.name"
                                :rules="required_field($t('required_name'))"
                                name="name"
                                type="text"
                                required
                            ><template #label>
                                {{$t('name')}}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                            <v-text-field 
                                v-model="user.lastName"
                                :rules="required_field($t('required_last_name'))"
                                name="lastName"
                                type="text"
                                required
                            ><template #label>
                                {{$t('last_name')}}<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                            <v-text-field 
                                v-model="user.email"
                                :rules="required_email($t('required_email'))"
                                name="email"
                                type="text"
                                required
                            ><template #label>
                                Email<span class="red--text"><strong>* </strong></span>
                            </template></v-text-field>
                            <v-text-field 
                                v-model="user.birthDate"
                                name="birthdate"
                                type="date"
                                :label="$t('birthdate')"
                            ></v-text-field>
                        </v-form>
                        </v-col>
                    </v-row>
                    </v-card-text>
                
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn
                            color="#1D8383"
                            text
                            @click="save()"
                        >{{$t('create')}}
                        </v-btn>
                    </v-card-actions>
                </v-container>
            </v-card>  
        </v-dialog>
    </div>
</template>

<script>
import RepositoryFactory from "@/repositories/RepositoryFactory";
const UsersRepository = RepositoryFactory.get("users");

export default {
  props:{
    dialog: {
      type: Boolean,
      required: true
    },
    isHomePage: {
      type: Boolean,
      required: true
    }
  },
  data: () => ({
        valid:true,
        user:{}
    }),
  methods: {
    validate () {
            this.$refs.form.validate()
    },
    reset () {
        this.$refs.form.reset()
    },
    resetValidation () {
        this.$refs.form.resetValidation()
    },
    required_field(msg){
        return [value => !!value || msg]
    },
    required_email(msg){
        return [ v => !!v || msg,
                    v => /.+@.+\..+/.test(v) || 'Email debe ser válido']
    },
    async save() {
            if (!this.$refs.form.validate()) {
                return;
            }
            try {
                await UsersRepository.save(this.user);
                this.$emit('close-dialog',false)
                if(this.isHomePage) {
                    this.$router.push({
                        name: "UserList"
                    })
                }else {
                    this.$router.go({
                        name: "UserList"
                    })
                };
            } catch (err) {
                this.$notify({
                    text: err.response.data.message,
                    type: "error"
                });
            }
        },
    close(){
        
        this.$emit('close-dialog',false)       
        this.reset()
    }
  }
}
</script>

<style scoped>
.v-card__title_dialog {
    background-color:#DDEEEB;
    font-weight: bold;
    justify-content: start;
}

</style>