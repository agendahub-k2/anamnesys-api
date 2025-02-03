# Etapa 1: Criando a imagem com base no JDK 21 (imagem base do JDK)
FROM openjdk:21-jdk-slim as build

# Definir o diretório de trabalho para o container
WORKDIR /app

# Copiar o arquivo Gradle e as dependências de construção
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Copiar o código-fonte do projeto
COPY src src

# Executar a build da aplicação
RUN ./gradlew build --no-daemon

# Etapa 2: Construindo a imagem final
FROM openjdk:21-jdk-slim

# Definir o diretório de trabalho
WORKDIR /app

# Copiar o JAR gerado da etapa de build para a imagem final
COPY --from=build /app/build/libs/*.jar app.jar

# Expor a porta onde a aplicação Spring Boot irá rodar
EXPOSE 8080

# Comando para rodar a aplicação Java dentro do container
ENTRYPOINT ["java", "-jar", "app.jar"]
