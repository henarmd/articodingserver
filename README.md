
# ArticodingServer

Primera versión de un servidor que almacena niveles de ArtiCoding, permitiendo su exportación e importación.

Un servidor node sencillo que utiliza mongoDN.

Para su instalación el equipo debe contar con las siguientes herramientas previas:
- MongoDB <https://www.mongodb.com/docs/manual/administration/install-community/>
- Node <https://nodejs.org/en/download/>

Una vez instaladas será necesario desplegar MongoDB y configurar el puerto del mismo en nuestra aplicación, bastaá con modificar la variable mongoDBen el archivo const mongoDB = server.js( se pasará a configuración más adelante).

Ahora para desplegar el servidor bastará con situar una consola en el fichero raíz y ejecutar el siguiente comando:

```console
npm start
```

Si todo va bien, se deberia mostrar el siguiente mensaje:
```console
> articodingserver@1.0.0 start
> node server.js

Articoding's Server está a tope en el 5000!
BBDD de niveles conectada!

```
Y ya está lista para recibir peticiones
