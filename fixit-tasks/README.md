# fixit-tasks

Microservicio de gestión de tareas y asignación automática para el sistema **Fixit**. Expone una API REST para la creación, seguimiento y asignación inteligente de tareas de mantenimiento técnico.

---

## Stack tecnológico

| Capa                      | Tecnología                          |
| ------------------------- | ----------------------------------- |
| Lenguaje                  | Java 21                             |
| Framework                 | Spring Boot 3.x                     |
| Seguridad                 | Spring Security + JWT (JJWT)        |
| Persistencia              | Spring Data JPA + PostgreSQL        |
| Mensajería                | RabbitMQ (Spring AMQP)              |
| Comunicación entre micros | OpenFeign (Spring Cloud)            |
| Documentación             | SpringDoc OpenAPI (Swagger UI)      |
| Build                     | Gradle 8                            |

---

## Arquitectura

Implementa arquitectura hexagonal (Ports & Adapters):

:

```
domain/          → Modelos, lógica de negocio, excepciones
application/     → Casos de uso, puertos (interfaces)
infraestructure/ → Adaptadores: REST, JPA, Feign, RabbitMQ,  JWT, Security
```

---
---

## Requisitos previos

- Java 21
- Gradle 8 (o usar el wrapper `./gradlew`)
- PostgreSQL 16 accesible
- RabbitMQ accesible
- Variables de entorno configuradas (ver sección siguiente)

---

## Variables de entorno

| Variable           | Descripción                            | Ejemplo / Default       |
| ------------------ | -------------------------------------- | ----------------------- |
| `SERVER_PORT`      | Puerto del servidor                    | `8090`                  |
| `DB_HOST`          | Host de PostgreSQL                     | `localhost`             |
| `DB_PORT`          | Puerto de PostgreSQL                   | `5432`                  |
| `DB_NAME`          | Nombre de la base de datos             | `fixit_db`              |
| `DB_USERNAME`      | Usuario de la DB                       | `postgres`              |
| `DB_PASSWORD`      | Contraseña de la DB                    | `postgres`              |
| `DB_SCHEMA`        | Esquema de la DB                       | `tasks`                 |
| `JWT_SECRET`       | Clave secreta para firmar tokens       | mínimo 32 caracteres    |
| `JWT_EXPIRATION`   | Tiempo de expiración del token (ms)    | `86400000`              |
| `RABBITMQ_HOST`    | Host de RabbitMQ                       | `localhost`             |
| `RABBITMQ_PORT`    | Puerto de RabbitMQ                     | `5672`                  |
| `RABBITMQ_USER`    | Usuario de RabbitMQ                    | `guest`                 |
| `RABBITMQ_PASS`    | Contraseña de RabbitMQ                 | `guest`                 |
| `USER_SERVICE_URL` | URL base del microservicio de usuarios | `http://localhost:8080` |

---

## Ejecución local

### Con Gradle

```bash
./gradlew bootRun
```

### Compilar JAR

```bash
./gradlew bootJar
java -jar build/libs/tasks-0.0.1-SNAPSHOT.jar
```

### Desde Docker (modo orquestado)

Ver el [README principal](../README.md) del repositorio `fixit-ops`.

---

## API Endpoints

La autenticación usa **JWT Bearer Token**. El token se obtiene desde el microservicio `fixit-user` en `/api/v1/auth/login`.

### Tareas

| Método   | Endpoint                           | Descripción                                | Rol requerido         |
| -------- | ---------------------------------- | ------------------------------------------ | --------------------- |
| `POST`   | `/api/v1/tasks`                    | Crear tarea con asignación automática      | `ADMIN`, `TECHNICIAN` |
| `GET`    | `/api/v1/tasks`                    | Listar todas las tareas                    | `ADMIN`, `TECHNICIAN` |
| `GET`    | `/api/v1/tasks/{id}`               | Obtener tarea por ID                       | `ADMIN`, `TECHNICIAN` |
| `GET`    | `/api/v1/tasks/technician/{id}`    | Listar tareas asignadas a un técnico       | `ADMIN`, `TECHNICIAN` |
| `PUT`    | `/api/v1/tasks/{id}`               | Actualizar tarea                           | `ADMIN`, `TECHNICIAN` |
| `DELETE` | `/api/v1/tasks/{id}`               | Eliminar tarea                             | `ADMIN`               |
| `POST`   | `/api/v1/tasks/{id}/assign-urgent` | Asignar manualmente una tarea urgente      | `ADMIN`               |
| `POST`   | `/api/v1/tasks/auto-assign/urgent` | Asignar automáticamente todas las urgentes | `ADMIN`               |
| `POST`   | `/api/v1/tasks/process-waiting`    | Procesar tareas en espera                  | `ADMIN`               |
| `PATCH`  | `/api/v1/tasks/{id}/start`         | Marcar tarea como iniciada                 | `TECHNICIAN`          |
| `PATCH`  | `/api/v1/tasks/{id}/complete`      | Marcar tarea como completada               | `TECHNICIAN`          |

---

## Lógica de asignación automática

Al crear una tarea (`POST /api/v1/tasks`), el sistema selecciona automáticamente al técnico más adecuado según su **habilidad** y **disponibilidad** actual. Las tareas urgentes sin técnico disponible quedan en estado de espera y pueden procesarse posteriormente con `/process-waiting` o asignarse manualmente con `/{id}/assign-urgent`.

---

## Documentación interactiva

Con el servicio corriendo, accede a Swagger UI:

http://localhost:{SERVER_PORT}/swagger-ui.html

La especificación OpenAPI está disponible en:

http://localhost:{SERVER_PORT}/v3/api-docs

---

## Comunicación con otros microservicios

| Microservicio        | Protocolo  | Propósito                                    |
|----------------------| ---------- | -------------------------------------------- |
| `fixit-user`         | HTTP/Feign | Consultar datos y disponibilidad de técnicos |
| `fixit-notification` | AMQP       | Publicar y consumir eventos de tareas        |



## Link documentación RFC

https://correoitmedu-my.sharepoint.com/:w:/g/personal/juancardona298991_correo_itm_edu_co/IQD3HY0l2hLbSYRhjCUG8e9-ASaVzCNjoARR1i1mRG8-l-c?e=OO3Cl3

## Link diagrama
https://drive.google.com/file/d/1O-AM9pXWbu-l8iiJdIN3rsX_1zYxM1iq/view?usp=sharing
