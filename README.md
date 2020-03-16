# Example using the Spring boot starter for Spring Data R2DBC
Source: https://github.com/pedro-tavares/spring-boot-r2dbc

## Setup
1. Spring Boot 2.2.4
2. Kotlin
3. Postgresql : https://www.postgresql.org/download/

This project demonstrates how to use R2DBC with Spring boot to reactively connect to a relational database. 

## Running the application
```
mvn spring-boot:run
```

```
http://localhost:8080/merchant/flux
http://localhost:8080/merchant/mono/name/{firstname}
http://localhost:8080/merchant/mono/email/{email}
```
