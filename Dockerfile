# Usa la imagen oficial de Java 17
FROM eclipse-temurin:17-jdk-alpine

# Crea el directorio de trabajo
WORKDIR /app

# Copia el archivo Maven Wrapper y el pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Descarga dependencias (para cachear)
RUN ./mvnw dependency:go-offline -B

# Copia el código fuente
COPY src src

# Empaqueta la aplicación
RUN ./mvnw clean package -DskipTests

# Expón el puerto 8080
EXPOSE 8080

# Ejecuta el JAR generado
CMD ["java", "-jar", "target/*.jar"]
