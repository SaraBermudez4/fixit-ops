# Fixit Ops — Entorno de Orquestación

Repositorio de infraestructura para el sistema **Fixit**: gestiona el ciclo de vida de tareas técnicas y el equipo de técnicos que las resuelve.

El entorno levanta los siguientes servicios mediante Docker Compose:

| Servicio         | Descripción                            | Puerto                  |
| ---------------- | -------------------------------------- | ----------------------- |
| `fixit-postgres` | Base de datos compartida PostgreSQL 16 | `5432`                  |
| `fixit-user`     | Microservicio de usuarios y técnicos   | configurable via `.env` |

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

Crea un archivo `.env` en la raíz del proyecto con el siguiente contenido (reemplaza los valores de ejemplo por los reales, consultar con el equipo):

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
```

> `DB_HOST` debe ser `postgres` (nombre del servicio en la red Docker), no `localhost`.

### 3. Levantar el entorno

```bash
docker compose up -d
```

La primera vez descarga las imágenes base y compila el proyecto con Gradle. Puede tardar varios minutos.

### 4. Verificar que los servicios están corriendo

```bash
docker compose ps
```

Deberías ver `fixit-postgres` y `fixit-user` con estado `running`.

### 5. Revisar los logs

```bash
# Todos los servicios
docker compose logs -f

# Solo fixit-user
docker compose logs -f fixit-user
```

---

## Acceso a los servicios

| Servicio           | URL                                                   |
| ------------------ | ----------------------------------------------------- |
| fixit-user API     | `http://localhost:{SERVER_PORT_USER}`                 |
| fixit-user Swagger | `http://localhost:{SERVER_PORT_USER}/swagger-ui.html` |
| PostgreSQL         | `localhost:5432`                                      |

---

## Comandos útiles

```bash
# Reconstruir la imagen de fixit-user (tras cambios en el código)
docker compose up --build -d

# Detener todos los servicios
docker compose down

# Detener y eliminar volúmenes (borra los datos de la DB)
docker compose down -v

# Ver el estado de los contenedores
docker compose ps
```

---

## Estructura del repositorio

```
fixit-ops/
├── docker-compose.yml       # Orquestación de servicios
├── init.sql                 # Inicialización de esquemas en PostgreSQL
├── .env                     # Variables de entorno (no versionado)
├── fixit-user/              # Microservicio de usuarios y técnicos
└── fixit-tasks/             # Microservicio de tareas (pendiente de dockerizar)
```
