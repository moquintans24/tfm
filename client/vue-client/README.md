# vue-client

Aplicación web implementada con Vue. Consta de un CRUD que realiza peticiones contra un servidor REST.

Los componentes Vue son definidos usando *single-file components*. Es decir, ficheros con extension `.vue` en los que se define el HTML, JS y CSS asociado a cada componente. Más información en la [documentación de Vue](https://vuejs.org/v2/guide/single-file-components.html).

## Ejecución

```bash
npm install
npm run serve
```
## Scripts

* `serve`: lanza el servidor de aplicaciones de parcel, empaqueta la aplicación y se queda en ejecución, refrescando el explorador web al cambiar el código fuente.
* `build`: empaqueta la aplicación.
* `lint`: limpia el código de la aplicación y muestra errores en el mismo (como variables no usadas o sintaxis incorrecta).

## Dependencias y utilidades

### Vue + vue router

Vue es un framework para crear interfaces web. Vue router es la librería que se encarga del enrutado de las diferentes páginas que compongan la aplicación (su función es equivalente a los controladores Spring que gestionan *qué se renderiza* con cada petición enviada).

* [Vue](https://github.com/vuejs/vue)
* [Vue router](https://router.vuejs.org/)

### Vue Notification

Un componente de Vue que nos permite mostrar alertas sin mucho trabajo.

* [Vue Notification](http://vue-notification.yev.io/)

### Vue CLI

Vue CLI es una herramienta que facilita el desarrollo de aplicaciones con vue. Permite crear esqueletos automáticamente y gestionar los plugins y dependencias de los mismos. Además, incluye un *empaquetador de aplicaciones web* (*web application bundler*), que se encarga de lanzar la aplicación en modo desarrollo de forma que se actualice automáticamente al cambiar el código fuente, o de generar la aplicación empaquetada lista para desplegar en producción. También proporciona una funcionalidad para reparar y validar el código fuente.

* [Vue CLI](https://cli.vuejs.org/)

### Vuetify

Vuetify es un framework basado en [Material Design](https://material.io/design/) que nos permite construir páginas web con un aspecto *cuidado* y *responsive* de manera sencilla. 

* [Vuetify](https://vuetifyjs.com/en/)

### Axios

Librería que nos permite realizar peticiones REST de manera más sencilla que usando el API *fetch*. 

* [Axios](https://github.com/axios/axios)

### EditorConfig

Plugin instalable en casi cualquier editor o IDE que nos permite unificar el estilo de la indentación, los saltos de línea y el encoding de los ficheros, entre otras cosas.

* [EditorConfig](https://editorconfig.org/)
