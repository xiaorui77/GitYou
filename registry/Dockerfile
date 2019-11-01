FROM armhfbuild/java
VOLUME /tmp
ADD target/registry-0.1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
