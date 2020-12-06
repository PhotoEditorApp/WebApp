FROM ubuntu:20.04
RUN apt-get update && apt-get upgrade -y && apt-get clean && apt-get install -y openjdk-14-jre-headless \
    curl \
    libgl1-mesa-glx \
    python3 \
    python3-dev \
    python3-distutils \
    python3-pip \
    build-essential \
    libssl-dev \
    libffi-dev
RUN update-alternatives --install /usr/bin/python python /usr/bin/python3 1
RUN update-alternatives --set python /usr/bin/python3
RUN pip3 install numpy \
    Pillow

ADD scripts scripts
ADD uploads uploads
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Dserver.port=$PORT","$JAVA_OPTS","-jar","/app.jar"]