FROM patavee/scipy-matplotlib-opencv-py3

FROM openjdk:14
EXPOSE 8080
COPY ./scripts ./scripts
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]