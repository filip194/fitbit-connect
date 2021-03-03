# Fitbit Connect (multi-module)

This is a training Spring Boot 2 multi-module Maven application with connection to Fitbit API over OAuth2.

## Table of contents

- [General](#general)
- [Starting application](#starting-application)
- [Storage](#storage)
- [Security](#security)
- [Technologies used](#technologies-used)
- [Notice](#notice)

### General

This application provides two APIs: _users_ and _fitbit_.

**users** API enables user registration, user search, user update and deletion.

**fitbit** API obtaining OAuth2 JWT token, refreshing token and getting user activities.

Both of these APIs are governed by security roles and as such can only be accessed by specific users.
For this demo case there are some preinstalled users when running app with H2 profile [admin, 123].
This user is granted all roles when logs in.

The application allows users to create account and login to Fitbit, connecting both accounts internally in a selected
database. Accounts are connected once user logs into official Fitbit webpage and Fitbit Connect obtaines its token. 
Token is then saved into selected database alongside user Fitbit ID and can be used later to get user activities.
Now, if you log in as ADMIN, you can get user activity data from Fitbit for each user.

### Starting application

To start the application you will need to go into application.properties in application module and provide
_fitbit.client.id_ and _fitbit.client.secret_, which you will obtain while registering new app on Fitbit Dev.

### Documentation

Documentation is automatically generated from integration tests, mostly by implementing code snippets created while
running integration tests. This is achieved by using REST Assured and Asciidoctor.
API documentation available at e.g.: http://localhost:9090/docs/doc_index.html.

For now, docs are manually pasted into static resources.

### Swagger-UI

http://localhost:9090/swagger-ui/ is the base url for the API.
Swagger is available only to admin [admin, 123].

### Storage

Fitbit Connect is using H2 in-memory database for developing purposes, and PostgreSQL database can be used in production
purposes. This is just an example of different databases being used for different scenarios.
Also, Flyway database migration is used to show how to migrate databases between versions/updates.

Data stored in database can be obtained by visiting swagger (e.g. http://localhost:9090/swagger-ui.html), and for
example, getting users can be done searching by username, and/or combined with paging (page size and page number).
Data can also be sorted ASC or DESC by username.

### Security

Security is implemented as basic security with usage of Bcrypt for storing passwords. There are several types of users:
 - anonymous user - can register
 - USER - searches users by ID or username 
 - MODERATOR - role not implemented; placeholder
 - ADMIN - all USER authorities + can update and delete users; can collect user activity data by obtaining it 
 over Fitbit API once user has registered and logged into Fitbit
 
### Technologies used

- Spring Boot 2
- Maven
- OAuth2
- Lombok
- Database layer:
    - H2 in-memory database
    - PostgreSQL database
    - Flyway database migration
- Virtualization:
    - Docker (used with PostgreSQL)
- AOP:
    - AspectJ (used for service logging)
- API exposure:
    - Swagger OpenAPI Specification 3 (http://localhost:9090/swagger-ui/)
- Documentation:
    - Spring REST Docs (Asciidoctor)
- Testing:
    - JUnit 5
    - WireMock
    - REST Assured

### Notice

This application runs correctly only when registered on Fitbit Dev portal (https://dev.fitbit.com/).
While registering your app you will have to use these values:

Property | Value
------------ | -------------
OAuth 2.0 Client ID | will be provided to you by Fitbit
Client Secret | will be provided to you by Fitbit
Callback URL | http://localhost:9090/api/fitbit/redirect
OAuth 2.0: Authorization URI | https://www.fitbit.com/oauth2/authorize
OAuth 2.0: Access/Refresh Token Request URI | https://api.fitbit.com/oauth2/token