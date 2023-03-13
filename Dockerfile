FROM eclipse-temurin:latest
VOLUME /tmp
COPY "build/libs/*.jar" app.jar
ENTRYPOINT ["java","-jar","/app.jar"]