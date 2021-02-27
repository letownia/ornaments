FROM openjdk:11-jre-slim
RUN apt-get update && apt-get install vim
COPY service/target/service-final.jar /ornaments/service-final.jar
ENV JAVA_OPTS ""
#CMD java $JAVA_OPTS -jar ./service.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /ornaments/service-final.jar
#ENTRYPOINT ["java","$JAVA_OPTS","-jar","/ornaments/service-final.jar"]