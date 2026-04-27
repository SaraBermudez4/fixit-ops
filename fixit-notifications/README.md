# fixit-notifications

Microservicio de notificaciones para el sistema **Fixit**. Escucha eventos de tareas publicados en RabbitMQ y envía notificaciones SMS a los técnicos a través de **Twilio**.

---

## Stack tecnológico

| Capa        | Tecnología                  |
| ----------- | --------------------------- |
| Lenguaje    | Java 21                     |
| Framework   | Spring Boot 3.x             |
| Mensajería  | RabbitMQ (Spring AMQP)      |
| Notificaciones | Twilio SMS API           |
| Build       | Gradle 8                    |

---

## Arquitectura

Microservicio orientado a eventos. No expone API REST — actúa únicamente como consumidor de mensajes:

RabbitMQ → Consumer (Listener) → Twilio SMS

---

## Requisitos previos

- Java 21
- Gradle 8 (o usar el wrapper `./gradlew`)
- RabbitMQ accesible
- Cuenta Twilio activa con número de teléfono habilitado para SMS
- Variables de entorno configuradas (ver sección siguiente)

---

## Variables de entorno

| Variable              | Descripción                           | Ejemplo / Default |
| --------------------- | ------------------------------------- |-------------------|
| `SERVER_PORT`         | Puerto del servidor                   | `8082`            |
| `RABBITMQ_HOST`       | Host de RabbitMQ                      | `localhost`       |
| `RABBITMQ_PORT`       | Puerto de RabbitMQ                    | `5672`            |
| `RABBITMQ_USER`       | Usuario de RabbitMQ                   | `guest`           |
| `RABBITMQ_PASS`       | Contraseña de RabbitMQ                | `guest`           |
| `TWILIO_ACCOUNT_SID`  | Account SID de Twilio                 | `ACxxxxxxxxxxxx`  |
| `TWILIO_AUTH_TOKEN`   | Auth Token de Twilio                  | *(ver equipo)*    |
| `TWILIO_PHONE_NUMBER` | Número de origen de los SMS (Twilio)  | `Cuenta Twilio`   |

---

## Ejecución local

### Con Gradle

```bash
./gradlew bootRun
```

### Compilar JAR

```bash
./gradlew bootJar
java -jar build/libs/notifications-0.0.1-SNAPSHOT.jar
```

### Desde Docker (modo orquestado)

Ver el [README principal](../README.md) del repositorio `fixit-ops`.

---

## Colas de RabbitMQ

Este microservicio escucha las siguientes colas. Son publicadas por `fixit-tasks`.

| Cola                                  | Evento               | Acción                                      |
| ------------------------------------- | -------------------- | ------------------------------------------- |
| `fixit.notifications.task.assigned`   | Tarea asignada       | Notifica al técnico por SMS sobre la nueva tarea |
| `fixit.notifications.task.completed`  | Tarea completada     | Notifica la finalización de la tarea        |

---

## Comunicación con otros microservicios

| Microservicio | Protocolo | Propósito                                          |
| ------------- | --------- | -------------------------------------------------- |
| `fixit-tasks` | AMQP      | Consume eventos de asignación y completado de tareas |
| Twilio API    | HTTPS     | Envío de notificaciones SMS a técnicos             |

## Link documentación RFC

https://correoitmedu-my.sharepoint.com/:w:/g/personal/juancardona298991_correo_itm_edu_co/IQD3HY0l2hLbSYRhjCUG8e9-ASaVzCNjoARR1i1mRG8-l-c?e=OO3Cl3

## Link diagrama
https://drive.google.com/file/d/1O-AM9pXWbu-l8iiJdIN3rsX_1zYxM1iq/view?usp=sharing
