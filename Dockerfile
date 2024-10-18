FROM openjdk:17-jdk

WORKDIR /app

COPY target/blogs-1.0.0.jar /app/blogs.jar

EXPOSE 8080

CMD ["java", "-jar", "blogs.jar"]
