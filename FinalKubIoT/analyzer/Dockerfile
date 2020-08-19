FROM tomcat:8.0-jre8
EXPOSE 8082
COPY analyzer.jar /
WORKDIR /
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/analyzer.jar"]
