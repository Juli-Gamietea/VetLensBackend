# Información académica
Este repositorio forma parte del Proyecto Final de Ingeniería de los estudiantes Gamietea Julián y Siciliano Franco de la carrera Ingeniería en Informática de la Universidad Argentina de la Empresa. Contactos: 

Julián Gamietea: jgamietea@uade.edu.ar

Franco Siciliano: frsiciliano@uade.edu.ar
# VetLensBackend
Este repositorio es el backend para la aplicación VetLens, el mismo esta desarrollado utilizando Java y Spring Boot.
Se recomienda tener instalado un IDE para facilitar la lectura del código y ejecución del mismo, el utilizado en el desarrollo fue Intellij IDEA.
Para poder utilizar la aplicación usted debe:
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

> Los distintos endpoints de la aplicación se disponibilizarán en /swagger-ui.html. Por ejemplo si corre la aplicación en el puerto 8080 el link al Swagger será:
> localhost:8080/swagger-ui.html

6. Una vez que se encuentre levantada la aplicación, dirigirse a la base de datos creada y correr la siguiente query en ella (como notaremos, el booteo de la aplicación Java generó todas las tablas necesarias para el funcionamiento)

```
USE vetlens;

INSERT INTO `diseases` VALUES 
(1,'Dermatitis Piotraumática','Resumen de Dermatitis Piotraumática'),
(2,'Miasis','Resumen de Miasis'),
(3,'Dermatofitosis','Resumen de Dermatofitosis'),
(4,'No discernible','No se especifica');

INSERT INTO treatments(name, source, summary, disease_id) VALUES 
('Lampara de Wood','https://www.novabel.es/lampara-wood-de-luz-ultravioleta#:~:text=La%20l%C3%A1mpara%20de%20Wood%20se,u%C3%B1as%20o%20detectar%20la%20psoriasis.','Permite distinguir un alto porcentaje de Microsporum canis que emiten una coloración azul-verdosa ante su exposición',3),
('Cultivo Fúngico','https://medlineplus.gov/spanish/pruebas-de-laboratorio/prueba-de-cultivo-fungico/','Una prueba de cultivo fúngico ayuda a diagnosticar las infecciones por hongos.',3),
('Prueba citológica','https://medlineplus.gov/spanish/ency/article/002323.htm#:~:text=Es%20el%20an%C3%A1lisis%20de%20c%C3%A9lulas','Usado para buscar infecciones virales en las células. Difiere de una biopsia en que únicamente se examinan células y no pedazos de tejido.',3),
('Limpieza zona afectada','https://dermatologiaveterinaria.unileon.es/dermatopatias/dermatitis_aguda_humeda.htm','Usar una solución salina suave o un antiséptico para limpiar la piel y eliminar la suciedad y el pelo alrededor de la lesión.',1),
('Secado y aislamiento','https://www.kivet.com/blog/el-collar-isabelino-uso/','Secar suavemente y evitar que el perro siga lamiendo o mordiendo la zona. Puedes utilizar un collar isabelino para evitar que se lama la lesión.',1),
('Tratamiento oral','https://www.agrivet.tienda/post/dermatitis-h%C3%BAmeda','Medicamentos antiinflamatorios por vía oral para controlar la inflamación y ayudar en la recuperación',1),
('Limpieza y desbridamiento','https://ddd.uab.cat/pub/clivetpeqani/clivetpeqani_a2012v32n3/clivetpeqaniv32n3p169.pdf','Eliminar todas las larvas y tejido muerto de la herida. ',2),
('Control de moscas','https://www.koniglab.com/bactrovet-plata-am-cura-todas-las-heridas-todas-las-bicheras/','Usar repelentes de moscas para evitar futuras infestaciones',2),
('Antibióticos y tratamiento tópico','https://www.periodicodeibiza.es/pitiusas/ibiza/2019/07/10/1093317/miasis-perros-trata.html#:~:text=Se%20debe%20aplicar%20povidona%20yodada','Administración de antibióticos para tratar infecciones secundarias',2);
```


## Consideraciones
Al estar en etapa de desarrollo la aplicación se encuentra consumiendo servicios web de capa gratuita (Mongo DBAtlas y Cloudinary) por lo cual se pide encarecidamente que
el tráfico de prueba sea moderado para poder mantenerse dentro de los valores suministrados gratuitamente por el proveedor.

## Contenerización
En el repositorio se encuentra un Dockerfile a partir del cuál puede construirse rápidamente una imagen del servicio, que permitirá correr el servidor de forma rápida e independiente de la máquina en la que se encuentra.

Para ello, primero se debe contar con Docker instalado en el dispositivo, y luego se deben ejecutar los siguientes comandos:

1. En el directorio donde se encuentra el Dockerfile correr: `docker build -t java-backend .`
2. Ejecutar el comando: `docker run -dp 127.0.0.1:8000:8000 java-backend`
