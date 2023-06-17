import Vue from "vue";
import i18n from "./plugins/i18n";
import store from "./common/store";
// Cargando vue-notification
import Notifications from "vue-notification";
Vue.use(Notifications);
Vue.directive('blur', {
  inserted: function (el) {
      el.onfocus = (ev) => ev.target.blur()
    
  }
});

import App from "./App.vue";
import router from "./router";
import vuetify from "./plugins/vuetify";

Vue.config.productionTip = false;

new Vue({
  mounted() {
    if (store.state.user.language!="") {
      localStorage.setItem("locale",store.state.user.language);
      location.reload()
    }
  },
  router,
  vuetify,
  i18n,
  render: h => h(App)
}).$mount("#app");
