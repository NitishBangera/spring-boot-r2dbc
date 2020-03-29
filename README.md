# Example using the Spring boot starter for Spring Data R2DBC

## Setup
1. Spring Boot 2.2.4
2. Kotlin 1.3.70
3. JRE 8
4. Postgresql : https://www.postgresql.org/download/

This project demonstrates how to use R2DBC with Spring boot to reactively connect to a relational database. 

## Running the application
```
mvn spring-boot:run
```

## Apis
```
http://localhost:8080/merchant/login
http://localhost:8080/merchant/all
http://localhost:8080/merchant/find/name/{firstname}
http://localhost:8080/merchant/find/email/{email}
```

Java version: https://github.com/pedro-tavares/spring-boot-r2dbc
