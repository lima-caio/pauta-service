FROM openjdk:8-jdk-alpine
ADD /build/libs/pauta-service-1.0.0.jar app.jar
ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://dockermongodb:27017/dg", "-Dkafka.enabled=false", "-Dlogging.level.org.apache.kafka.clients.NetworkClient=ERROR", "-jar", "app.jar"]