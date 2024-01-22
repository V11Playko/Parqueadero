#  Parqueadero - Reto #

Este proyecto se enfoca en el desarrollo de una APIREST diseñada para gestionar eficientemente el control de vehículos en parqueaderos asociados. La principal funcionalidad se centra en la administración en tiempo real de la entrada y salida de vehículos, así como la implementación de un sistema de historial que registra información detallada sobre los vehículos que han utilizado los parqueaderos en el pasado.

## Instalación y configuración ##

Antes de empezar, asegúrate de tener instalados y configurados los siguientes elementos:

1. Java 17
2. Gradle 8.5
3. Base de datos PostgreSQL (con las variables de entorno USER y PASSWORD ya configuradas).

Para instalar y configurar la aplicación en un entorno local, sigue estos pasos:

1. Clona este repositorio en tu máquina local.
2. Importa el proyecto en tu IDE favorito.
3. Instala las dependencias del proyecto usando Gradle.
4. Crea dos bases de datos PostgreSQL con el nombre "usuarios-service" y "parqueadero-service" respectivamente.
5. Configura las variables de entorno necesarias para la base de datos. 
   - Abre tu IDE y encuentra la configuración de ejecución para el microservicio 'parking-service' o en el que estes actualmente.
   - En el caso de IntelliJ IDEA, puedes hacer esto yendo a 'Run/Debug Configurations'.
   - Busca la configuración correspondiente al microservicio 'parking-service' y haz clic para editarla.
   - Dentro de la configuración, busca la sección 'Environment variables'.
   - Agrega las siguientes variables de entorno con los valores que hayas decidido para tu base de datos PostgreSQL:
     - USER: es el username que tienes en la base de datos.

     (En el caso de PgAdmin se encuentra en PostgreSQL->Properties->Connection->Username)

     - PASSWORD: la contraseña de la base de datos.
6. Ejecuta los tres microservicios.(usuarios-service, parqueadero-service, y mensajeria-service)
7. Consulta la documentación de la API de User-Service para obtener más detalles sobre los endpoints disponibles en `http://localhost:8091/swagger-ui/index.html#/`.
8. Consulta la documentación de la API de Parking-Service para obtener más detalles sobre los endpoints disponibles en `http://localhost:8092/swagger-ui/index.html#`.
9. Consulta la documentación de la API de Messaging-Service para obtener más detalles sobre los endpoints disponibles en `http://localhost:8093/swagger-ui/index.html#`.
10. Tambien puedes ver y exportar la [colección de postman.](docs/Parqueadero-Reto.postman_collection.json)

## Licencia ##

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo LICENSE para más información.

## Comentario ##
Si tienes algún comentario sobre el repositorio, por favor dímelo para poder mejorar :)

- 📫 Cómo contactarme **heinnervega20@gmail.com**