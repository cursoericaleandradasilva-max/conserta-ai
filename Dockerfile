# Etapa 1: Build com Maven e Java 21
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Instala Maven
RUN apk add --no-cache maven

# Copia apenas o pom.xml primeiro para aproveitar cache de dependencias Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o codigo-fonte e compila
COPY src ./src
RUN mvn package -DskipTests -B

# Etapa 2: Extrai o JAR em formato "exploded" (nao-comprimido)
# Evita um bug conhecido do Spring Boot 3.2+ onde o scan de @EnableJpaRepositories
# pode falhar (encontrar 0 interfaces) dependendo do filesystem do container,
# por causa do protocolo "nested:" usado para ler classes de dentro do JAR comprimido.
FROM eclipse-temurin:21-jre-alpine AS extractor
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --destination extracted

# Etapa 3: Imagem final enxuta para producao
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia a aplicacao ja extraida (BOOT-INF/classes vira pasta real, nao mais dentro do jar)
COPY --from=extractor /app/extracted/app/ ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]