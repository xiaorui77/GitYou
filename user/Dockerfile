FROM armhfbuild/java
# COPY target/lib/ ./lib/
ADD target/user-service-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
