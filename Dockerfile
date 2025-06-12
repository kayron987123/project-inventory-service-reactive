# Imagen base con JDK 21
FROM eclipse-temurin:21.0.7_6-jdk

# Establece el directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Descargar dependencias sin compilar (más rápido)
RUN ./mvnw dependency:go-offline

# Copiar el código fuente de la aplicación
COPY src/ src/

# Compilar y empaquetar la aplicación (genera el .jar)
RUN ./mvnw clean package -Dtest=\!*RepositoryTest

# Exponer el puerto
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "target/inventory-service-0.0.1-SNAPSHOT.jar"]
