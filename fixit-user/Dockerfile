# ─── ETAPA 1: COMPILACIÓN ───────────────────────────────────────────────────
FROM amazoncorretto:21-alpine AS builder

# Alpine no tiene 'bash' ni 'findutils' por defecto, Gradle los necesita
RUN apk add --no-cache bash findutils

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src/ src/

RUN ./gradlew bootJar --no-daemon -x test

# ─── ETAPA 2: IMAGEN FINAL ──────────────────────────────────────────────────
FROM amazoncorretto:21-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder /app/build/libs/*.jar app.jar

USER appuser

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
