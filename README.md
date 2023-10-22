# VetLensBackend
Este repositorio es el backend para la aplicación VetLens, el mismo esta desarrollado utilizando Java y Spring Boot.
Se recomienda tener instalado un IDE para facilitar la lectura del código y ejecución del mismo, el utilizado en el desarrollo fue Intellij IDEA
Para poder utilizar la aplicación usted debe
1. Tener instalado Git, MySQL y Java 17
2. Descargar el repositorio en la ruta en la que se encuentra en su dispositivo mediante 'git clone https://github.com/Juli-Gamietea/VetLensBackend.git'
3. Asegurarse de encontrarse sobre la branch 'main' (con git checkout main en caso de no estarlo)
4. Crear una base de datos en MySQL, por defecto el proyecto posee configurada la base de datos **_vetlens_** con user y password **_root_**, en
caso de desear cambiarle la configuración debe dirigirse al archivo [application.properties](https://github.com/Juli-Gamietea/VetLensBackend/blob/main/src/main/resources/application.properties) y modificar las siguientes lineas:
```
spring.datasource.url=jdbc:mysql://localhost:3306/vetlens
spring.datasource.username=root
spring.datasource.password=root
```
5. Correr el archivo [VetlensApplication.java](https://github.com/Juli-Gamietea/VetLensBackend/blob/main/src/main/java/com/api/vetlens/VetlensApplication.java)

## Consideraciones
Al estar en etapa de desarrollo la aplicación se encuentra consumiendo servicios web de capa gratuita (Mongo DBAtlas y Cloudinary) por lo cual se pide encarecidamente que
el tráfico de prueba sea moderado para poder mantenerse dentro de los valores suministrados gratuitamente por el proveedor.
