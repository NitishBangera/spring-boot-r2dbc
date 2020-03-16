# Example using the Spring boot starter for Spring Data R2DBC
Source: https://github.com/pedro-tavares/spring-boot-r2dbc

## Setup

This project demonstrates how to use R2DBC with Spring boot to reactively connect to a relational database. The database I'm using in this example is PostgreSQL. To set up a PostgreSQL database, you can run the Docker container by using the following command:

Additionally, you can run the Spring boot application by using Maven:

```
mvn spring-boot:run
```

```
http://localhost:8080/merchant/flux
http://localhost:8080/merchant/mono/name/{firstname}
http://localhost:8080/merchant/mono/email/{email}
```
