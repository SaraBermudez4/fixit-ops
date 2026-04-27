# fixit-user

Microservicio de gestión de usuarios y técnicos para el sistema **Fixit**. Expone una API REST para autenticación y administración del equipo técnico.

---

## Stack tecnológico

| Capa                      | Tecnología                          |
| ------------------------- | ----------------------------------- |
| Lenguaje                  | Java 21                             |
| Framework                 | Spring Boot 3.4.3                   |
| Seguridad                 | Spring Security + JWT (JJWT 0.11.5) |
| Persistencia              | Spring Data JPA + PostgreSQL        |
| Comunicación entre micros | OpenFeign (Spring Cloud 2024.0.0)   |
| Mappers                   | MapStruct 1.6.3                     |
| Documentación             | SpringDoc OpenAPI (Swagger UI)      |
| Build                     | Gradle 8                            |

---

## Arquitectura

Implementa arquitectura hexagonal (Ports & Adapters):

```
domain/          → Modelos, lógica de negocio, excepciones
application/     → Casos de uso, puertos (interfaces)
infraestructure/ → Adaptadores: REST, JPA, Feign, JWT, Security
```

---

## Requisitos previos

- Java 21
- Gradle 8 (o usar el wrapper `./gradlew`)
- PostgreSQL 16 accesible
- Variables de entorno configuradas (ver sección siguiente)

---

## Variables de entorno

| Variable          | Descripción                          | Ejemplo                 |
| ----------------- | ------------------------------------ | ----------------------- |
| `SERVER_PORT`     | Puerto del servidor                  | `8080`                  |
| `DB_HOST`         | Host de PostgreSQL                   | `localhost`             |
| `DB_PORT`         | Puerto de PostgreSQL                 | `5432`                  |
| `DB_NAME`         | Nombre de la base de datos           | `fixit_db`              |
| `DB_USERNAME`     | Usuario de la DB                     | `fixit_user`            |
| `DB_PASSWORD`     | Contraseña de la DB                  | `fixit_pass`            |
| `DB_SCHEMA`       | Esquema de la DB                     | `users`                 |
| `JWT_SECRET`      | Clave secreta para firmar tokens     | mínimo 32 caracteres    |
| `JWT_EXPIRATION`  | Tiempo de expiración del token (ms)  | `3600000`               |
| `FIXIT_TASKS_URL` | URL base del microservicio de tareas | `http://localhost:8090` |

---

## Ejecución local

### Con Gradle

```bash
./gradlew bootRun
```

### Compilar JAR

```bash
./gradlew bootJar
java -jar build/libs/user-0.0.1-SNAPSHOT.jar
```

### Desde Docker (modo orquestado)

Ver el [README principal](../README.md) del repositorio `fixit-ops`.

---

## API Endpoints

La autenticación usa **JWT Bearer Token**. Para obtener un token usa el endpoint `/api/v1/auth/login`.

### Autenticación

| Método | Endpoint             | Descripción    | Acceso  |
| ------ | -------------------- | -------------- | ------- |
| `POST` | `/api/v1/auth/login` | Iniciar sesión | Público |

### Técnicos

| Método   | Endpoint                                  | Descripción                 | Rol requerido         |
| -------- | ----------------------------------------- | --------------------------- | --------------------- |
| `POST`   | `/api/v1/technicians`                     | Crear técnico               | `ADMIN`               |
| `GET`    | `/api/v1/technicians`                     | Listar todos los técnicos   | `ADMIN`               |
| `GET`    | `/api/v1/technicians/{id}`                | Obtener técnico por ID      | `ADMIN`               |
| `GET`    | `/api/v1/technicians/category/{category}` | Filtrar por categoría       | `ADMIN`               |
| `PUT`    | `/api/v1/technicians/{id}`                | Actualizar datos operativos | `ADMIN`, `TECHNICIAN` |
| `PUT`    | `/api/v1/technicians/{id}/category`       | Cambiar categoría           | `ADMIN`               |
| `GET`    | `/api/v1/technicians/{id}/workload`       | Consultar carga de trabajo  | `ADMIN`, `TECHNICIAN` |
| `DELETE` | `/api/v1/technicians/{id}`                | Eliminar técnico            | `ADMIN`               |

**Categorías válidas:** `JUNIOR`, `SEMI_SENIOR`, `SENIOR`, `MASTER`

---

## Documentación interactiva

Con el servicio corriendo, accede a Swagger UI:

```
http://localhost:{SERVER_PORT}/swagger-ui.html
```

---

## Usuario administrador por defecto

Al iniciar, se inserta automáticamente un usuario admin si no existe:

| Campo    | Valor                   |
| -------- | ----------------------- |
| Email    | `admin@fixit.com`       |
| Password | *(consultar al equipo)* |
| Rol      | `ADMIN`                 |

## Link documentación RFC

https://correoitmedu-my.sharepoint.com/:w:/g/personal/juancardona298991_correo_itm_edu_co/IQD3HY0l2hLbSYRhjCUG8e9-ASaVzCNjoARR1i1mRG8-l-c?e=OO3Cl3

## Link diagrama
https://drive.google.com/file/d/1O-AM9pXWbu-l8iiJdIN3rsX_1zYxM1iq/view?usp=sharing
