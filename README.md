# Sistema de alquiler de herramientas

## 1. Nombre del proyecto

**Sistema de alquiler de herramientas**

## 2. Datos del estudiante

* **Nombre completo:** Iván Eduardo del Cid Véliz.
* **Número de carné:** 9941-25-20822
* **Curso:** Programación II.

## 3. Descripción del problema

El proyecto busca facilitar la administración de un negocio de alquiler de herramientas. Permite registrar herramientas, consultar cuáles están disponibles, gestionar los alquileres y registrar las devoluciones.

También permite calcular el costo estimado de cada alquiler, identificar devoluciones atrasadas y consultar información general sobre los alquileres registrados.

## 4. Tecnologías utilizadas

* **Java 25:** lenguaje de programación.
* **Spring Boot 4.1.1:** desarrollo del backend.
* **Spring Web:** creación de la API REST.
* **Spring Data JPA:** acceso y persistencia de datos.
* **Hibernate:** implementación de JPA.
* **PostgreSQL 18:** base de datos.
* **Maven:** administración de dependencias y compilación.
* **HTML:** estructura de la interfaz web.
* **CSS:** diseño y estilos.
* **JavaScript:** interacción con el backend.
* **IntelliJ IDEA:** entorno de desarrollo.
* **Postman:** pruebas de la API.

## 5. Estructura general del sistema

El proyecto utiliza una estructura basada en Maven y una arquitectura por capas.

```text
alquiler-herramientas/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .mvn/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/alquilerherramientas/
│       │       ├── AlquilerHerramientasApplication.java
│       │       ├── controller/
│       │       ├── dto/
│       │       ├── entity/
│       │       ├── exception/
│       │       ├── repository/
│       │       └── service/
│       └── resources/
│           ├── application.properties
│           └── static/
│               ├── index.html
│               ├── style.css
│               └── script.js
└── README.md
```

La estructura mostrada es una referencia y debe coincidir con los archivos presentes en el repositorio.

**Descripción de las capas:**

* **Controller:** recibe las solicitudes HTTP y devuelve las respuestas de la API.
* **Service:** contiene las reglas del negocio y la lógica de los alquileres.
* **Repository:** permite consultar y guardar información en PostgreSQL mediante JPA.
* **Entity:** representa las entidades `Herramienta` y `Alquiler`.
* **DTO:** define los datos utilizados para recibir y responder solicitudes.
* **Exception:** contiene las excepciones específicas del sistema.
* **GlobalExceptionHandler:** centraliza el tratamiento de errores.
* **Static:** contiene los archivos de la interfaz web.

## 6. Requisitos para ejecutar el proyecto

Para ejecutar la aplicación se necesita:

1. Tener Java 25 instalado.
2. Contar con PostgreSQL instalado y funcionando.
3. Crear la base de datos correspondiente.
4. Configurar las credenciales de conexión.
5. Abrir el proyecto completo en IntelliJ IDEA o utilizar la terminal.
6. Tener conexión a internet la primera vez que Maven descargue las dependencias.

El proyecto incluye Maven Wrapper para facilitar la compilación sin necesidad de instalar Maven por separado.

## 7. Configuración de PostgreSQL

La base de datos utilizada por el proyecto se llama `alquiler_herramientas`.

Se puede crear desde PostgreSQL o pgAdmin con la siguiente instrucción:

```sql
CREATE DATABASE alquiler_herramientas;
```

La conexión se configura en el archivo:

`src/main/resources/application.properties`

Ejemplo de configuración:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/alquiler_herramientas
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Se deben establecer las variables de entorno `DB_USERNAME` y `DB_PASSWORD` con las credenciales de PostgreSQL correspondientes.

La configuración debe coincidir con la que utiliza realmente el proyecto. No se deben publicar contraseñas personales en el repositorio.

## 8. Instrucciones para iniciar el backend

### Desde IntelliJ IDEA

1. Abrir la carpeta raíz `alquiler-herramientas`.
2. Esperar a que IntelliJ reconozca y cargue las dependencias de Maven.
3. Verificar que PostgreSQL esté funcionando.
4. Revisar la configuración de conexión en `application.properties`.
5. Abrir la clase `AlquilerHerramientasApplication.java`.
6. Ejecutar el método principal de la aplicación.
7. Comprobar en la consola que Spring Boot haya iniciado correctamente.

### Desde la terminal de Windows

Abrir PowerShell en la carpeta raíz del proyecto y ejecutar:

```powershell
.\mvnw.cmd clean package
```

Si la compilación termina correctamente, iniciar el backend con:

```powershell
.\mvnw.cmd spring-boot:run
```

El backend utiliza el puerto `8080`, salvo que se haya configurado otro en el proyecto.

La dirección base de la API será:

`http://localhost:8080`

## 9. Instrucciones para abrir el frontend

La interfaz web se encuentra en:

`src/main/resources/static/`

Contiene los siguientes archivos:

* `index.html`: estructura de la página.
* `style.css`: estilos de la interfaz.
* `script.js`: funciones que interactúan con la API.

Con el backend en ejecución, se puede intentar acceder a la interfaz desde el navegador mediante:

`http://localhost:8080/index.html`

Si la aplicación está configurada para servir los archivos estáticos de Spring Boot, la página se abrirá desde esa dirección.

Si se utiliza otro servidor para abrir el frontend, se debe verificar que las solicitudes de JavaScript apunten a la dirección correcta del backend y que la configuración permita dichas solicitudes.

## 10. Descripción de los endpoints

La API permite administrar herramientas y alquileres mediante solicitudes HTTP.

### Endpoints de herramientas

| Método | Endpoint                    | Descripción                                    |
| ------ | --------------------------- | ---------------------------------------------- |
| GET    | `/herramientas`             | Consulta todas las herramientas registradas.   |
| GET    | `/herramientas/disponibles` | Consulta las herramientas disponibles.         |
| GET    | `/herramientas/{id}`        | Busca una herramienta por su identificador.    |
| POST   | `/herramientas`             | Registra una nueva herramienta.                |
| PUT    | `/herramientas/{id}`        | Actualiza los datos de una herramienta.        |
| DELETE | `/herramientas/{id}`        | Elimina una herramienta cuando está permitido. |

### Endpoints de alquileres

| Método | Endpoint                      | Descripción                                        |
| ------ | ----------------------------- | -------------------------------------------------- |
| GET    | `/alquileres`                 | Consulta todos los alquileres.                     |
| GET    | `/alquileres/activos`         | Consulta los alquileres activos.                   |
| GET    | `/alquileres/atrasados`       | Consulta los alquileres atrasados.                 |
| GET    | `/alquileres/resumen`         | Consulta un resumen de los alquileres.             |
| GET    | `/alquileres/{id}`            | Busca un alquiler por su identificador.            |
| POST   | `/alquileres`                 | Registra un nuevo alquiler.                        |
| PUT    | `/alquileres/{id}/devolucion` | Registra la devolución de una herramienta.         |
| DELETE | `/alquileres/{id}`            | Elimina un alquiler cuando las reglas lo permiten. |

Los endpoints indicados deben contrastarse con las anotaciones de los controladores de la versión final.

## 11. Reglas principales del negocio

El sistema aplica las siguientes reglas:

1. **Disponibilidad:** una herramienta no puede alquilarse si ya se encuentra ocupada.
2. **Costo estimado:** se calcula multiplicando los días de alquiler por la tarifa diaria de la herramienta.
3. **Fechas:** las fechas del alquiler y de la devolución prevista deben ser válidas.
4. **Registro del alquiler:** cada alquiler se asocia con una herramienta mediante una relación entre las entidades `Alquiler` y `Herramienta`.
5. **Devolución:** al registrar la devolución, el alquiler queda marcado como devuelto y la herramienta puede volver a estar disponible.
6. **Retrasos:** un alquiler se considera atrasado cuando continúa activo después de la fecha prevista de devolución.
7. **Devoluciones duplicadas:** el sistema debe impedir que se registre más de una devolución para el mismo alquiler.
8. **Recursos inexistentes:** si se solicita una herramienta o un alquiler que no existe, la API debe informar el error correspondiente.
9. **Validaciones:** los datos obligatorios deben cumplir las restricciones definidas para cada solicitud.
10. **Eliminación:** no se deben permitir operaciones que contradigan las reglas del sistema, como eliminar un alquiler que todavía está activo, si así lo establece la implementación.

## 12. Manejo de errores

El sistema incluye excepciones específicas y un `GlobalExceptionHandler` para centralizar las respuestas de error.

Los principales códigos HTTP utilizados son:

| Código | Descripción                                       |
| ------ | ------------------------------------------------- |
| 200    | Solicitud procesada correctamente.                |
| 201    | Recurso creado correctamente, cuando corresponde. |
| 204    | Operación completada sin contenido de respuesta.  |
| 400    | Datos de entrada inválidos.                       |
| 404    | Recurso no encontrado.                            |
| 409    | Conflicto con el estado actual del recurso.       |
| 500    | Error interno inesperado.                         |

## 13. Pruebas recomendadas

Las funcionalidades se pueden probar utilizando Postman:

1. Registrar una herramienta.
2. Consultar las herramientas registradas.
3. Consultar las herramientas disponibles.
4. Registrar un alquiler.
5. Comprobar el costo estimado.
6. Intentar alquilar una herramienta que ya está ocupada.
7. Enviar datos inválidos para comprobar las validaciones.
8. Consultar los alquileres activos y atrasados.
9. Registrar una devolución.
10. Comprobar que la herramienta vuelva a estar disponible.
11. Intentar registrar nuevamente la devolución.
12. Consultar el resumen general de los alquileres.

Estas pruebas permiten verificar tanto las operaciones normales como el comportamiento ante errores.
