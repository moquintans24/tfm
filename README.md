# Aplicación web de gestión y distribución decontenido digital para una editora digital.

**Requisitos previos para la instalción**:
  - Instalación de NodeJs y Npm para la gestión de paquetes. Versión de NodeJs v16.17.1, versión de npm 8.15.0.
  - Instalación de Apache Maven para la gestión del servidor web. Versión: Apache Maven 3.9.1.
  - Instalación de Git para la ejecución del servidor H5P. Versión: 2.40.0.windows.1.
  - Instalación del servidor H5P ’npm install @lumieducation/h5p-server’.
  - Instalación de los componentes de H5P: ’npm install @lumieducation/h5p-webcomponents’.

Antes de la ejecución de la aplicación es necesaria la configuración de la base de datos:
  - Creación de una base de datos en PostgreSQL.
  - Configuración del fichero /service/src/main/resources/application.yml con la conexión a la base de datos, indicando el usuario y contraseña generados.

**Servidor web**
  - Se ejecuta en el puerto 8080. Desde el directorio rest-service:
    - Instalar dependencias ’npm install’.
    - Ejecutar ’mvn spring-boot:run’.
**Servidor H5P**
  - Si se está instalando en Windows, es necesario utilizar Git bash. Se ejecuta en el puerto 8081. Desde el directorio h5p-server:
    - Instalar dependencias ’npm install’.
    - Ejecutar ’npm start’
**Cliente**
  - Desde el directorio vue-client:
    - Instalar dependencias ’npm install’.
    - Ejecutar ’npm run serve’.
