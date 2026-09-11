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

# Etapa 2: Imagem final - descompacta o jar e roda via classpath direto
# Evita por completo o loader do Spring Boot (org.springframework.boot.loader),
# que sempre le BOOT-INF/classes via o esquema "nested:", independente de
# extracao. Rodando via -cp comum, o Spring Data JPA escaneia diretorios e
# jars reais, sem depender desse mecanismo.
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# unzip necessario para descompactar o jar
RUN apk add --no-cache unzip

COPY --from=builder /app/target/*.jar app.jar
RUN unzip -q app.jar -d unpacked && rm app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -cp unpacked/BOOT-INF/classes:unpacked/BOOT-INF/lib/* com.ericajavaproagent.consertaai.ConsertaAiApplication"]