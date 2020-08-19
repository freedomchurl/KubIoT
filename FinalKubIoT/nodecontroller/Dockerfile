FROM tomcat:8.0-jre8
EXPOSE 8083
COPY nodecontroller.jar /
WORKDIR /
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/nodecontroller.jar"]
