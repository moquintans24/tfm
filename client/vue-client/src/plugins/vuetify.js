import Vue from "vue";
import Vuetify, { colors }  from "vuetify/lib";

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: "mdi"
  },
  theme: {
    themes: { 
        dark: {
          background:"#f3f9f8"
        }
     }
  }
});