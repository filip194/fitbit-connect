# Fitbit Connect (multi-module)

This is a training Spring Boot 2 multi-module Maven application with connection to Fitbit API over OAuth2.

## Table of contents

- [General](#general)
- [Storage](#storage)
- [Security](#security)
- [Technologies used](#technologies-used)
- [Notice](#notice)

### General

This application provides two APIs: _users_ and _fitbit_.

_users_ API enables user registration, user search, user update and deletion.

_fitbit_ API obtaining OAuth2 JWT token, refreshing token and getting user activities.

Both of these APIs are governed by security roles and as such can only be accessed by specific users.
For this demo case there are some preinstalled users when running app with H2 profile: admin 123.
This user can grant you all roles.

The application allows users to create account and login to Fitbit, connecting both accounts internally in selected
database. Accounts are connected once user logs into official Fitbit webapage and Fitbit Connect obtaines its token. 
Token is then saved into selected database alongside user Fitbit ID and can be used later to get user activities.
Now, if you login as ADMIN, you can get user activity data from Fitbit for specific user.

### Documentation

Documentation is automatically generated from integration tests, mostly by implementing code snippets created while
running integration tests. This is achieved by using REST Assured and Asciidoctor.
API documentation available at: http://localhost:9090/docs/doc_index.html.

For now, docs are manually pasted into static resources.

### Storage

Fitbit Connect is using H2 in-memory database for developing purposes, and PostgreSQL database can be used in production
purposes. This is just an example of different databases being used for different scenarios.
Beside, Flyway database migration is used to show how to migrate databases between versions/updates.

Data stored in database can be obtained by visiting swagger (e.g. http://localhost:9090/swagger-ui.html), and for
example, getting users can be done searching by username, and/or combined with paging (page size and page number).
Data can also be sorted ASC or DESC by username.

### Security

Security is implemented as basic security with usage of Bcrypt for storing passwords. There are several types of users:
 - anonymous user - can register
 - USER - searches users by ID or username 
 - MODERATOR - role not implemented; placeholder
 - ADMIN - all USER authorities + can update and delete users; can collect user activity data by obtaining it 
 from Fitbit API service once user has registered and logged into Fitbit
 
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
    - Swagger 2 (can be visited e.g.: http://localhost:9090/swagger-ui.html)
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
