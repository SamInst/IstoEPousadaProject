FROM maven:3.8.5-openjdk-17 as pom-builder
WORKDIR /root
COPY pom.xml .
RUN mvn dependency:resolve

FROM maven:3.8.5-openjdk-17 as jar-builder
WORKDIR /root/app
COPY --from=pom-builder /root/.m2 ..
COPY . .
RUN mvn package -Dmaven.test.skip

FROM openjdk:17-alpine
COPY --from=jar-builder /root/app/target/PousadaIstoE-0.0.1-SNAPSHOT.jar .
ENTRYPOINT exec java -jar PousadaIstoE-0.0.1-SNAPSHOT.jar
