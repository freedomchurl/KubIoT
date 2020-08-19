FROM registry:latest
FROM tomcat:8.0-jre8
EXPOSE 8081
COPY DataCollector.jar /
WORKDIR /
ENTRYPOINT ["java","-jar","DataCollector.jar"]
