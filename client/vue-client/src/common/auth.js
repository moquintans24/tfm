import HTTP from "./http";
import store from "./store";
import i18n from '@/plugins/i18n';

const user = store.state.user;
const app = store.state.app;

export default {
  login,
  logout,
  forgotPassword,
  resetPassword,
  saveLoginPassword,
  reloadUserData,
  getToken,
  isAdmin,
  isEditor,
  isTeacher,
  isStudent,
  isAuthenticationChecked: isAuthenticationChecked()
};

function login(credentials) {
  return HTTP.post("authenticate", credentials).then(response => {
    _saveToken(response.data.token);
    return _authenticate();
  });
}

function logout() {
  _removeToken();
  _removeBookCode();
  user.id = "";
  user.login = "";
  user.authority = "";
  user.logged = false;
  user.name = "";
  user.linkImage = "";
  user.initials = "",
  app.drawer = false;
  i18n.locale = "es-ES"
}

function forgotPassword(email_login){
  return HTTP.post("forgot-password",{},{
    params: {
        email_login: email_login
    }});
} 

function resetPassword(id,validationString,password){
  return HTTP.post(`reset-password/${id}`,{},{
    params: {
        password: password,
        validationString: validationString
    }});
} 

function saveLoginPassword(id,login,password, validationString){
  return HTTP.post(`new-login-password/${id}`,{},{
    params: {
        login: login,
        password: password,
        validationString: validationString
    }});
}

function getToken() {
  return localStorage.getItem("token");
}

function isAdmin() {
  return user.authority == "ADMIN";
}

function isEditor() {
  return user.authority == "EDITOR";
}

function isTeacher() {
  return user.authority == "TEACHER";
}

function isStudent() {
  return user.authority == "USER"; //estudiante
}

// usamos localStorage para guardar el token, de forma
// que sea persistente (se inhabilita con el tiempo o
// al hacer logout)
function _saveToken(token) {
  localStorage.setItem("token", token);
}

function _removeToken() {
  localStorage.removeItem("token");
}

//Si el usuario quiere añadir un libro a su lista de libros sin iniciar sesión, almacenamos el 
// código y lo borramos al hacer logout
function _removeBookCode(){
  localStorage.removeItem("path_add_book_code");
}

// si tenemos el token guardado, esta petición se hará
// con el filtro definido en http-common y por tanto nos
// devolverá el usuario logueado
function _authenticate() {
  return HTTP.get("account").then(response => {
    user.id = response.data.id;
    user.login = response.data.login;
    user.language = response.data.language;
    user.authority = response.data.authority;
    user.logged = true;
    if(response.data.authority != 'ADMIN'){
      user.name = response.data.name;
      user.linkImage = response.data.linkImage;
      user.initials  = response.data.name.charAt(0).concat(response.data.lastName.charAt(0))
    }
    
    app.drawer = true;
    if(response.data.language != null){
      i18n.locale = response.data.language
    }
    return user;
  });
}

function reloadUserData(data){
  if(user.authority != 'ADMIN'){
    user.name = data.name;
    user.linkImage = data.linkImage;
    user.initials  = data.name.charAt(0).concat(data.lastName.charAt(0))
  }
}

// este método devuelve una promesa que se resuelve cuando
// se haya comprobado si el token, de existir, es válido o no
function isAuthenticationChecked() {
  return new Promise(res => {
    if (getToken()) {
      _authenticate()
        .catch(() => logout())
        .finally(() => res(true));
    } else {
      res(true);
    }
  });
}
