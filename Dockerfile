FROM maven:3.9.9-amazoncorretto-21
COPY . .
RUN mvn clean package -Dmaven.test.skip

ENTRYPOINT ["java", "-jar", "/target/ProjectSem4-0.0.1-SNAPSHOT.jar"]

