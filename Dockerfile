FROM maven:3.8.5-openjdk-17
LABEL mantainer="Luis Vegas" \
      build="docker build -t ecommerce-java ." \
      run="docker run -e DATASOURCE_PASSWORD='DB_PASSWORD' -e DATASOURCE_USERNAME='DB_USERNAME' -e DATASOURCE_URL='DB_URI' -p 8080:8080 --rm ecommerce-java"
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

ARG DATASOURCE_PASSWORD
ARG DATASOURCE_USERNAME
ARG DATASOURCE_URL

RUN export DATASOURCE_PASSWORD=$DATASOURCE_PASSWORD
RUN export DATASOURCE_USERNAME=$DATASOURCE_USERNAME
RUN export DATASOURCE_URL=$DATASOURCE_URL

EXPOSE 8080
CMD ["java","-jar","./target/ecommerce-0.0.1-SNAPSHOT.jar"]
