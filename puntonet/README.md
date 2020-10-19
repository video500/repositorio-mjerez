Examen para puntonet
===
Creado por Manuel A. Jerez
Fecha: 18-08-2020

El proyecto está desarrollado en Java, hibernate con interfaz primefaces 8. La estructura inicial de archivos está creada con JBOSS Forge.

Se ha probado con Wildfly 18, y el datasource que utiliza es el que viene configurado por defecto en este servidor.

Es decir, la conexión a una base de datos H2 (Se crea en RAM, así que se reestablece al reiniciar la aplicación)
<jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source>

La entidad principal a la que se hizo el crud, Ticket, está anotada como entidad. El resto de valores a usar, se creó en una clase llamada parámetros, que se llena en tiempo de ejecución.

Entiéndase que en un proyecto más grande, estos valores serían implementados en diferentes entidades, según sea el caso.

No se realizaron restricciones ni validaciones de datos, más allá del tipo, debido a la poca información que provee la imagen inicial.