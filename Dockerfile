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

# Etapa 2: Imagem final enxuta para producao
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR gerado da etapa de build
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
