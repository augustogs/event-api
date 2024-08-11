# Etapa 1: Construir a aplicação
FROM maven:3.8.6-openjdk-18-slim AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml e o diretório src
COPY pom.xml .
COPY src ./src

# Executa o comando para construir o JAR da aplicação
RUN mvn clean install -DskipTests

# Etapa 2: Criar a imagem final
FROM openjdk:18-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR construído da etapa anterior
COPY --from=build /app/target/eventAPI-0.0.1-SNAPSHOT.jar /app/eventAPI.jar

# Expor a porta 8080
EXPOSE 8080

# Define o comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/eventAPI.jar"]
