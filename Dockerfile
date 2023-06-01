FROM maven:3.8.7-openjdk-18 as pom-builder
WORKDIR /root
COPY pom.xml .
RUN mvn dependency:resolve

FROM maven:3.8.7-openjdk-18 as jar-builder
WORKDIR /root/app
COPY --from=pom-builder /root/.m2 ..
COPY . .
RUN mvn package -Dmaven.test.skip

FROM openjdk:18-alpine
COPY --from=jar-builder /root/app/target/app.jar .
ENTRYPOINT exec java -jar app.jar
