<template>
  <v-layout text-center wrap align-center>
    <v-flex mb-4>
      <h1 class="display-2 font-weight-bold">
        {{ $t('home') }}
      </h1>
    </v-flex>
    <v-flex xs12>
      <v-container fluid >
        <v-row dense align="center" justify="center">
          <v-col  v-for="(card,index) in getItems" :key="index" :cols="card.flex" v-if="card.auth.includes(authority)">
            <v-card  class="mx-auto"  width="350px" height="300px">
              <v-img
                :src="card.src"
                class="white--text align-end"
                gradient="to bottom, rgba(112, 128, 130,.1), rgba(112, 128, 130,.5)"
                height="220px" width="350px"
              ></v-img>
              <v-card-title>
                  <router-link :to="{ name: card.linkName }">
                    {{ card.title }}
                  </router-link>
                  <v-spacer></v-spacer>
                  <v-btn icon color="grey" v-if="isTeacher && card.title == $t('class_groups')">
                    <v-icon @click="dialog2 = true">mdi-plus</v-icon>
                  </v-btn>
                  <v-btn icon color="grey" v-if="isAdmin && card.title ==  $t('user_management')">
                    <v-icon @click="dialog = true">mdi-plus</v-icon>
                  </v-btn>
                  <v-btn icon color="grey" v-if="isStudent && card.title ==  $t('class_groups')">
                    <v-icon @click="dialogAddClass = true">mdi-plus</v-icon>
                  </v-btn>
                </v-card-title>
            </v-card>
          </v-col>
        </v-row>
        <NewClass v-if="isTeacher" :dialog2="dialog2" :isHomePage="true" @close-dialog="dialog2 =false" />
        <NewUser v-if="isAdmin" :dialog="dialog" :isHomePage="true" @close-dialog="dialog =false" />
        <NewClassGroupSubscription v-if="isStudent" :dialogAddClass="dialogAddClass" :isHomePage="true" :isBookPage="false" @close-dialog="dialogAddClass = false" />
      </v-container>
    </v-flex>
  </v-layout>
</template>
 


<script>
import auth from "../common/auth";
import store from "../common/store";
import NewUser from "../entities/user/NewUser.vue";
import NewClass from "../entities/class/NewClass.vue";
import NewClassGroupSubscription from "../entities/class/NewClassGroupSubscription.vue";

export default {
  name: 'Home',
  components: {
    NewUser,
    NewClass,
    NewClassGroupSubscription
  },
  data: () => ({
      dialog:false,
      dialog2:false,
      dialogAddClass:false,
      authority:store.state.user.authority
    }),
  computed: {
    isAdmin() {
      return auth.isAdmin();
    },
    isTeacher() {
      return auth.isTeacher();
    },
    isStudent() {
      return auth.isStudent();
    },
    getItems () {
      const t = this.$t.bind(this)
        return [
                { title: t('books'),src: require('../assets/books.png'), flex: 3,linkName:'BookList',auth:['ADMIN','USER','TEACHER','EDITOR'] },
                { title: t('user_management'),src: require('../assets/users_management.png'),flex: 3 ,linkName:'UserList',auth:['ADMIN']  },
                { title: t('class_groups'),src: require('../assets/class_group.jpg'),flex: 3,linkName:'ClassList',auth:['TEACHER','USER']   },
                { title: t('statistics'),src: require('../assets/statistics.png'),flex: 3,linkName:'Statistics',auth:['ADMIN']   },
            ]
    }
  },
  created(){
    this.dialog = false
    this.dialog2 = false
    this.dialogAddClass = false
  }
};
</script>

<style scoped>
h1 {
  color:#22555A;
  margin-top: 30px;
  margin-bottom: 30px;
  justify-content: center;
}
.v-card__title_ {
  color:#22555A;
  font-weight: bold;
  justify-content: center;
}
</style>