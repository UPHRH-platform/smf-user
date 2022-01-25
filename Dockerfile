FROM openjdk:8
MAINTAINER haridas <haridas.kakunje@tarento.com>

ADD target/user-0.0.1-SNAPSHOT.jar user-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/user-0.0.1-SNAPSHOT.jar"]
EXPOSE 8090
