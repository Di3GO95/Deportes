# IMPORTANTE

Este proyecto ha sido realizado como práctica de la asignatura "Aplicaciones Distribuidas" de la Facultad de Informática de la Universidad de Murcia.

## Breve descripcion 📋

Desarrollo de un sistema de información sobre una arquitectura web donde el negocio se encuentra distribuido.

El sistema se encarga de organizar eventos deportivos entre aficionados amateurs para deportes basados en un resultado de tanteo (futbol, baloncesto, balonmano, etc.).

### Funcionamiento 🚀

El sistema permitirá a los usuarios administradores registrar una temporada con
un nombre (e.j. "Futbol sala 17/18- Ayto. Murcia"), pudiendo indicar el lugar de
celebración de los partidos y el número mínimo de usuarios participantes en cada
partido de la temporada. Además, se pueden añadir usuarios participantes a una
temporada durante su registro o en cualquier otro momento. Solo los usuarios
registrados en una temporada podrán participar en los partidos de la temporada.
Un usuario participante será registrado indicando: usuario y clave, mail, movil de
contacto y opcionalmente un foto (imagen). Los datos de los usuarios registrados
pueden modificarse en cualquier momento. Los usuarios deberán hacer un login para
poder entrar en el sistema.

Solo los usuarios administradores podran registrar partidos en una temporada,
indicando tan solo la fecha de celebración del partido.

Una vez registrado un partido pendiente de celebrar (si la fecha actual es anterior o
igual a la fecha del partido), los usuarios participantes (de la temporada del partido)
confirmarán su asistencia a los partidos. Los usuarios dispondrán en el sistema de
una opción para ver los partidos pendientes de celebración.

Solo los usuarios administradores podrán registrar alineaciones de un partido
pendiente cuando el número de confirmaciones para el partido sea igual o superior
al mínimo indicado en la temporada. Entonces deberán indicar el nombre de la
alineación (opcional), el color de las equipaciones y la lista de usuarios que
componen la alineación (solo usuarios que han confirmado su asistencia al partido).

Solo los usuarios administradores podrán introducir el tanteo (resultado) de una
alineación si la fecha actual es igual o posterior a la del partido.

El sistema permitirá mostrar los datos de un partido, mostrando la fecha y
los datos de sus alineaciones (nombre, color, tanteo y lista de jugadores).

Además se permitirá buscar partidos por los siguientes filtros (es posible combinar
filtros): por temporada, por rango de fechas y por jugador participante.

Por último, el sistema mandará un mensaje a los usuarios participantes de una temporada cada
vez que se cree un partido. Todos los usuarios del sistema deberán recibir un mensaje
de notificación sobre la creación del nuevo partido. Dicho mensaje debe contener
información sobre la fecha del partido y se le invitará a que confirme su
participación en él. El mensaje de respuesta se mandará a un destino único que tiene
el sistema para recibir avisos de confirmación.

### Tecnologías 🛠️

HTML y CSS para la vista.

Java, usando MVC así como patrones de diseño como Singleton o DAO.

Persistencia usando MySQL como base de datos. La persistencia se implementa haciendo uso del patrón DAO, haciendo
uso de JPA. También hay partes disponibles en JDBC, aunque sólo parcialmente.

La vista se ha implementado haciendo uso de JSF 2 + Facelets, así como la librería PrimeFaces.

Se ha distribuido la aplicación. De forma que tenemos un proyecto JAVA con las vistas, así como un controlador, que llama a otro controlador distribuído, en otro proyecto. La distribución se ha conseguido mediante EJB3.

Se ha implementado un sistema de envío de mensajes no persistido en la base de datos, haciendo uso de JMS.

## Autor ✒️

* **Diego Valera** - *Desarrollo completo de la aplicación* - [Di3GO95](https://github.com/Di3GO95/)

## Licencia 📄

Este proyecto está bajo la licencia MIT - mira el archivo [LICENSE.md](LICENSE.md) para detalles.
