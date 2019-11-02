FROM armhfbuild/java
VOLUME /opt/common
ADD target/gateway-0.1.0.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
