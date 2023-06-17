import Vue from "vue";
import Router from "vue-router";

import auth from "./common/auth";
import store from "./common/store";
const user = store.state.user;
import { NotFound, Login, Home, Register, Statistics,ForgotPassword, ResetPassword, NewLoginPassword } from "./components";
import bookRouter from "./entities/book/book.router";
import classRouter from "./entities/class/class.router";
import contentManagerRouter from "./entities/user/user.router";
import codeRouter from "./entities/code/code.router";
import activityRouter from "./entities/activity/activity.router";

Vue.use(Router);

const routes = [
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: { public: true, isLoginPage: true }
  },
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: { public: false }
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
    meta: { public: true }
  },
  {
    path: "/statistics",
    name: "Statistics",
    component: Statistics,
    meta: { public: false }
  },
  {
    path: "/books/edit/:bookId(\\d+)/:lessonId(\\d+)/activity/:activityId(\\d+)?/h5pEditor/:idH5PFile?",
    name: "H5PEditor",
    component: () =>
      import( "./components/H5PEditor.vue"),
    meta: { public: false }
  },
  {
    path: "/h5pPlayer/activity/:activityId/:idH5Pfile/:interactionId?",
    name: "H5PPlayer",
    component: () =>
      import( "./components/H5PPlayer.vue"),
    meta: { public: false }
  },
  {
    path: "/forgot-password",
    name: "ForgotPassword",
    component: ForgotPassword,
    meta: { public: true }
  },
  {
    path: "/reset-password/:id/:validationString",
    name: "ResetPassword",
    component: ResetPassword,
    meta: { public: true }
  },
  {
    path: "/new-login-password/:id/:validationString",
    name: "NewLoginPassword",
    component: NewLoginPassword,
    meta: { public: true }
  },
  {
    path: "/about",
    name: "About",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "./components/About.vue"),
    meta: { public: true }
  },
  { path: "*", component: NotFound, meta: { public: true } }
];

// A las rutas definidas en este fichero les añadimos rutas importadas
const router = new Router({
  // routes: routes.concat(postRouter).concat(profileRouter).concat(bookRouter).concat(classRouter)
  // .concat(contentManagerRouter).concat(codeRouter)
  routes: routes.concat(bookRouter).concat(classRouter).concat(contentManagerRouter).concat(codeRouter).concat(activityRouter)
});

router.beforeEach((to, from, next) => {
  // Lo primero que hacemos antes de cargar ninguna ruta es comprobar si
  // el usuario está autenticado (revisando el token)
  auth.isAuthenticationChecked.finally(() => {
    // por defecto, el usuario debe estar autenticado para acceder a las rutas
    const requiresAuth = !to.meta.public;

    const requiredAuthority = to.meta.authority;
    const userIsLogged = user.logged;
    const loggedUserAuthority = user.authority;

    if (requiresAuth) {
      if (userIsLogged) {
        if (requiredAuthority && requiredAuthority != loggedUserAuthority) {
          // usuario logueado pero sin permisos
          Vue.notify({
            text:
              "Access is not allowed for the current user. Try to log again.",
            type: "error"
          });
          auth.logout();
          next("/login");
        } else {
          // usuario logueado y con permisos adecuados
          next();
        }
      } else {
        // usuario no está logueado
        // si quiere añadir un libro a su lista de libros antes de iniciar sesión almacenamos el código, para que, en 
        // el momento en que inicie sesión le lleve a la página con el código ya introducido para añadir el libro nuevo
        if(to.params.code) localStorage.setItem("path_add_book_code",to.params.code);
        Vue.notify({
          text: "This page requires authentication.",
          type: "error"
        });
        next("/login");
      }
    } else {
      // página pública
      if (userIsLogged && to.meta.isLoginPage) {
        // si estamos logueados no hace falta volver a mostrar el login
        next({ name: "Home", replace: true });
      } else {
        next();
      }
    }
  });
});

export default router;
