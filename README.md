# Fixit Ops — Entorno de Orquestación

Repositorio de infraestructura para el sistema **Fixit**: gestiona el ciclo de vida de tareas técnicas y el equipo de técnicos que las resuelve.

El entorno levanta los siguientes servicios mediante Docker Compose:

| Servicio         | Descripción                            | Puerto |
| ---------------- | -------------------------------------- | ------ |
| `fixit-postgres` | Base de datos compartida PostgreSQL 16 | `5432` |
| `fixit-user`     | Microservicio de usuarios y técnicos   | `8080` |
| `fixit-tasks`    | Microservicio de tareas y asignación   | `8090` |

---

## Requisitos previos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) >= 24.x
- Git

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/SaraBermudez4/fixit-ops.git
cd fixit-ops
```

### 2. Crear el archivo de variables de entorno

Crea un archivo `.env` en la raíz del proyecto con el siguiente contenido:

```env
# PostgreSQL
POSTGRES_USER=fixit_user
POSTGRES_PASSWORD=fixit_pass
POSTGRES_DB=fixit_db

# fixit-user — Conexión a la DB
DB_HOST=postgres
DB_PORT=5432
DB_NAME=fixit_db
DB_USERNAME=fixit_user
DB_PASSWORD=fixit_pass
DB_SCHEMA_USER=users

# fixit-user — Servidor
SERVER_PORT_USER=8080

# fixit-user — JWT
JWT_SECRET=un_secreto_seguro_de_al_menos_32_caracteres
JWT_EXPIRATION=3600000

# fixit-user — Comunicación con fixit-tasks
FIXIT_TASKS_URL=http://fixit-tasks:8090

# fixit-tasks — Servidor
SERVER_PORT_TASKS=8090

# fixit-tasks — Base de datos
DB_SCHEMA_TASK=tasks

# fixit-tasks — Comunicación con fixit-user
FIXIT_USER_URL=http://fixit-user:8080
```

> `DB_HOST` debe ser `postgres` (nombre del servicio en la red Docker), no `localhost`.

### 3. Levantar el entorno

```bash
docker compose up --build
```

La primera vez descarga las imágenes base y compila el proyecto con Gradle. Puede tardar varios minutos.

### 4. Verificar que los servicios están corriendo

```bash
docker compose ps
```

Deberías ver `fixit-postgres`, `fixit-user` y `fixit-tasks` con estado `running`.

### 5. Revisar los logs

```bash
# Todos los servicios
docker compose logs -f

# Solo fixit-user
docker compose logs -f fixit-user

# Solo fixit-tasks
docker compose logs -f fixit-tasks
```

---

## Acceso a los servicios

| Servicio            | URL                                     |
| ------------------- | --------------------------------------- |
| fixit-user API      | `http://localhost:8080`                 |
| fixit-user Swagger  | `http://localhost:8080/swagger-ui.html` |
| fixit-tasks API     | `http://localhost:8090`                 |
| fixit-tasks Swagger | `http://localhost:8090/swagger-ui.html` |
| PostgreSQL          | `localhost:5432`                        |

---

## Autenticación

El sistema utiliza autenticación basada en JWT.

### Login

```http
POST /api/v1/auth/login
```

Ejemplo de cuerpo:

```json
{
  "email": "admin@fixit.com",
  "password": "Admin123*"
}
```

La respuesta retorna un token JWT. Para consumir endpoints protegidos se debe enviar el token en el header:

```http
Authorization: Bearer TOKEN
```

---

## Gestión de técnicos

Los técnicos se gestionan desde el microservicio `fixit-user`.

### Crear técnico

```http
POST /api/v1/technicians
```

Ejemplo de cuerpo:

```json
{
  "dni": "1001",
  "name": "Juan",
  "lastName": "Perez",
  "email": "juan@test.com",
  "password": "Juan123*",
  "phoneNumber": "3001111111",
  "category": "JUNIOR"
}
```

---

## Gestión de tareas

Las tareas se gestionan desde el microservicio `fixit-tasks`.

### Crear tarea con asignación automática

```http
POST /api/v1/tasks
```

Header requerido:

```http
Authorization: Bearer TOKEN
```

Ejemplo de cuerpo:

```json
{
  "name": "Servidor caído",
  "description": "Sistema crítico caído",
  "priority": "URGENT"
}
```

---

## Lógica de negocio

- Asignación automática de tareas a técnicos disponibles.
- La asignación se realiza con base en:
  - Prioridad de la tarea.
  - Categoría del técnico.
  - Capacidad del técnico, medida en puntos disponibles.

### Regla especial

- Las tareas con prioridad `URGENT` se asignan preferentemente a técnicos `MASTER`.

---

## Comandos útiles

```bash
# Reconstruir la imagen de los servicios
docker compose up --build -d

# Detener todos los servicios
docker compose down

# Detener y eliminar volúmenes (borra los datos de la DB)
docker compose down -v

# Ver el estado de los contenedores
docker compose ps

# Ver logs de todos los servicios
docker compose logs -f

# Ver logs de fixit-user
docker compose logs -f fixit-user

# Ver logs de fixit-tasks
docker compose logs -f fixit-tasks
```

---

## Estructura del repositorio

```text
fixit-ops/
├── docker-compose.yml       # Orquestación de servicios
├── init.sql                 # Inicialización de esquemas en PostgreSQL
├── .env                     # Variables de entorno (no versionado)
├── fixit-user/              # Microservicio de usuarios y técnicos
└── fixit-tasks/             # Microservicio de tareas dockerizado
```
