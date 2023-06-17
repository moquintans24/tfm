<template>
  <v-app v-bind:style="{ background: $vuetify.theme.themes.dark.background}">
    <!-- Notificaciones usando vue-notification -->
    <notifications :max="3" :width="400" position="top center" />

    <!-- Menú lateral -->
    <v-navigation-drawer v-model="app.drawer" app dark color="#22555A" expand-on-hover :clipped="$vuetify.breakpoint.lgAndUp">
      
      <v-list dense>
        <v-list-item to="/login" v-if="!isLogged" color="#EDFFFC">
          <v-list-item-icon>
            <v-icon>mdi-login</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>Login</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <!-- Enlazando una ruta usando el nombre en vez del path. Esto permite
             añadir parámetros si fuera necesario directamente desde el HTML. -->
        <v-list-item :to="{ name: 'Home' }" v-if="isLogged" exact color="#EDFFFC">
          <v-list-item-icon>
            <v-icon>mdi-home</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ $t('home') }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item to="/books" color="#EDFFFC" v-if="isLogged">
          <v-list-item-icon>
            <v-icon>mdi-book-open-variant</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ $t('books') }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item to="/classes" color="#EDFFFC" v-if="isLogged && !isAdmin && !isEditor">
          <v-list-item-icon>
            <!-- <v-icon >human-male-board</v-icon> -->
            <span class="material-icons">
              cast_for_education
            </span>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ $t('class_groups') }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item to="/userList" color="#EDFFFC" v-if="isLogged && isAdmin">
          <v-list-item-icon>
            <v-icon>mdi-account-multiple</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ $t('user_management') }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-list-item :to="{ name: 'Statistics' }" color="#EDFFFC" v-if="isLogged && isAdmin">
          <v-list-item-icon>
            <v-icon>mdi-chart-bar</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ $t('statistics') }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>

        <v-divider></v-divider>
      </v-list>

      <template v-slot:append v-if="isLogged" two-line>
        <v-divider></v-divider>
          <v-list-item @click="logout()" color="#EDFFFC">
          <v-list-item-icon>
            <v-icon>mdi-logout</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ $t('logout') }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </template>
      
      
    </v-navigation-drawer>

    <!-- Barra horizontal superior -->
    <v-app-bar :clipped-left="$vuetify.breakpoint.lgAndUp" app color="#22555A" dark v-if="isLogged">
      <v-app-bar-nav-icon
        @click.stop="app.drawer = !app.drawer"
      ></v-app-bar-nav-icon>
      <v-toolbar-title>
        <v-img class="mr-3" src="@/assets/digital_content_.png" height="55px" width="75px"> </v-img>
      </v-toolbar-title>
      <v-toolbar-title>{{ $t('digital_content') }}</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-toolbar-items>
        <v-list-item two-line>
          <v-list-item-content>
            <v-list-item-title>{{ user.name }}</v-list-item-title>
            <v-list-item-subtitle
              >{{ $t('logged_as') }} {{ authority_ }}</v-list-item-subtitle
            >
          </v-list-item-content>
        </v-list-item>
        <v-btn icon x-large  :to="{ name: 'UserProfile',params: { id: user.id }}" v-if="isLogged && !isAdmin">
          <v-avatar color="#EDFFFC" size="48">
            <img v-if="getUserImage != 'default_user_image.jpg'"
              :src="getRouteImage"
            />
            <!-- Si no hay imagen poner las iniciales -->
            <h1 v-else  style="color:#22555A;">{{ getUserInitials }}</h1>
          </v-avatar>
        </v-btn>
      </v-toolbar-items>
    </v-app-bar>

    <v-content>
      <v-container fluid>
        <!-- Contenido gestionado por vue-router -->
        <router-view />
      </v-container>
    </v-content>
  </v-app>
</template>

<script>
import store from "./common/store";
import auth from "./common/auth";
import i18n from '@/plugins/i18n';

export default {
  data: () => ({
    app: store.state.app,
    user: store.state.user,
    userId: store.state.user.id,
    items: [
                {
                  language: 'es-ES',
                  name: 'Español',
                  image: require('./assets/Flag_of_Spain.svg')
                },
                {
                  language: 'gl-ES',
                  name: "Galego",
                  image: require('./assets/Flag_of_Galicia.svg')
                },
                {
                  language: 'en-GB',
                  name: "Inglés",
                  image: require('./assets/Flag_of_the_United_Kingdom.svg')
                }
              ] 
  }),
  computed: {
    isLogged() {
      return this.user.logged;
    },
    isAdmin() {
        return auth.isAdmin();
    },
    isEditor() {
      return auth.isEditor();
    },
    authority_(){
      const t = this.$t.bind(this)
      if(auth.isAdmin()){
        return t('admin')
        
      }
      if(auth.isStudent()){
        return t('student')
      }
      if(auth.isTeacher()){
        return t('teacher')
      }
      if(auth.isEditor()){
        return t('editor')
      }
    },
    getUserImage(){
      return store.state.user.linkImage;
    },
    getUserInitials(){
      return store.state.user.initials;
    },  
    getRouteImage(){
      return 'http://localhost:8080/api/users/images/'+store.state.user.linkImage
    }
  },
  methods: {
    logout() {
      auth.logout();
      //Después de hacer el logout volvemos a la página de Login
      if (this.$router.currentRoute.name != "Login") {
        this.$router.push({ name: "Login" });
      }
    },
    changeLanguage(id,item) { 
      i18n.locale = item.language
    }
  }
};
</script>

<style lang="scss" src="./App.scss"></style>
