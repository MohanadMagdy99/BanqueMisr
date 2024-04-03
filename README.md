# Banque Misr

This is a Task-Management Application Challenges

## Setup

1. Make sure Java >= 17 is installed
2. Make sure Docker is installed
3. Make sure IntelliJ is installed
6. Check versions

```bash
mvn clean install
docker-compose up
```

7. change variable spring.liquibase.enabled to false and run the application to create tables with JPA
8. change the variable back to true and rerun the application for the seed data to be inserted

There are 3 types of users

Admin User created by default in the PostConstructInit Class with its configs
and has actions of creating users and oversee the users

Employee who has allowed actions of Employee role like preview his profile check assigned tasks

Manager who has allowed actions of Manager role like assign task to employee, update employee and so on

in order to test the apis you will find a postman collection along with the code

you can access mails btw sent through this url http://localhost:1080/#/

P.S the application supports both pagination and filtering for filtering any get api that 
has paging to it the filteration for Tasks are the fields of TaskFilter.java 
just append to the query param any field from the class to the query params
and it will read it normally same thing for users but with fields in UserFilter.java 



