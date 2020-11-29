FROM patavee/scipy-matplotlib-opencv-py3
COPY ./scripts ./scripts

FROM openjdk:13-jdk-alpine3.10
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
EXPOSE 8080
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]