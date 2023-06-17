<!-- Diálogo para suscribirse a un grupo de clase  -->
<template>
    <div class="text-center">
        <!-- Solo el alumno se va a suscribir a un grupo de clase-->
        <v-dialog v-model="dialogAddClass" persistent max-width="600px">
            <v-card>
                <v-card-title class="v-card__title_dialog" >{{$t('new_class_subscription')}} 
                    <v-spacer></v-spacer>
                    <v-btn icon  @click.stop="closeDialog">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-card-title>
                <v-container>

                    <v-form ref="form" v-model="valid" lazy-validation>
                        <div style="text-align:left" > 
                            <v-card-text class="v-card__text_dialog">{{$t('add_class_group_code_and_book_code')}} </v-card-text>
                        </div>
                        <div style="text-align:left" > 
                            <v-card-text >
                                <div style="text-align:left" > {{ $t('class_group_code') }}<span class="red--text"><strong>* </strong></span> </div>
                                
                                <v-text-field 
                                    v-model="classGroupCode" solo ref="classGroupCode"
                                    :rules="required_field($t('required_class_group_code'))"
                                    required
                                ></v-text-field>
                                <div style="text-align:left" > {{ $t('book_code') }}<span class="red--text"><strong>* </strong></span> </div>
                                <v-text-field 
                                    v-model="bookCode" solo ref="bookCode"
                                    :rules="required_field($t('required_book_code'))"
                                    :disabled="isBookPage"
                                    required
                                ></v-text-field>
                            </v-card-text>
                        </div>
                    </v-form>
                    <v-card-actions>
                        <v-btn color="red" text @click.stop="closeDialog">{{ $t('cancel') }}</v-btn>
                        <v-spacer></v-spacer>
                        <v-btn color="green darken-1" text @click.stop="newClassSubscription()">{{ $t('ok') }}</v-btn>
                    </v-card-actions>
                </v-container>  
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
import auth from "../../common/auth";
import store from "../../common/store";
import RepositoryFactory from "@/repositories/RepositoryFactory";
const SubscriptionsRepository = RepositoryFactory.get("subscriptions");

export default {
    name: 'NewClassGroupSubscription',
    props:{
        dialogAddClass: {
            type: Boolean,
            required: true
        },
        isHomePage: {
            type: Boolean,
            required: true
        },
        isBookPage:{
            type: Boolean,
            required:false
        }

    },
    data: () => ({
        valid:true,
        classGroupCode:null,
        bookCode:null
    }),
    computed: {
        isAdmin() {
            return auth.isAdmin();
        }
        
    },
    async created() {
        //Suscripcion del usuario
        if(this.isBookPage){
            await SubscriptionsRepository.getByBookIdAndUserId(this.$route.params.id,store.state.user.id).then(subscription =>{
                this.bookCode = subscription.code.code
            });  
        }

    },
    methods: {
        async newClassSubscription(){
            if(!this.$refs.form.validate()) {
                return;
            }
            await SubscriptionsRepository.updateUserSubscriptionsAddUserToClassGroup(this.classGroupCode,store.state.user.id,this.bookCode).then(async () => {
                
                if(this.isHomePage || this.isBookPage){
                    this.$router.push({
                        name: "ClassList"
                    })
                }else{
                    this.$router.go({
                        name: "ClassList"
                    })
                }
            }).catch(err => {
                this.$notify({
                    
                    text: err.response.data.message,
                    type: "error"
                });
            });
            this.closeDialog()
            this.$refs.form.resetValidation()
        },
        required_field(msg){
            return [value => !!value || msg]
        },
        validate () {
            this.$refs.form.validate()
        },
        resetValidation () {
            this.$refs.form.resetValidation()
        },
        reset () {
            this.$refs.form.reset()
        },
        closeDialog(){
            this.$emit('close-dialog',false)   
            this.$refs.form.resetValidation()    
            if(!this.isBookPage) this.reset()
            else this.classGroupCode = null
        }
    }
};
</script>
    
<style scoped>
.v-card__text_dialog {
    font-weight: bold;
    text-align: start;
}

.v-card__title_dialog {
background-color:#DDEEEB;
font-weight: bold;
justify-content: start;
}
</style>