import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.UserType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wiremock.com.google.common.collect.ImmutableMap;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class UserApiTests extends TestSupport {

    private static final String USER_API_PATH = "/api/users";

    @BeforeEach
    public void cleanDatabase() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        createRoles();
    }

    @Test
    public void shouldRegisterUser() {
        final ValidatableResponse response = given(documentationSpec)
                .filter(document("register_user"))
                .log().all()
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ImmutableMap.builder()
                        .put("username", USERNAME_USER)
                        .put("password", PASSWORD)
                        .put("type", "USER")
                        .put("name", "user_name")
                        .put("last_name", "user_last_name")
                        .put("email", "usertest@test.com")
                        .put("age", 24)
                        .build())
                .when()
                .request(HttpMethod.POST.name(), getPath() + "/register")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        response.assertThat().body("user_id", not(empty()));
        response.assertThat().body("username", is(USERNAME_USER));
        response.assertThat().body("type", is(UserType.USER.name()));
    }

    @Test
    public void shouldListAllUsers() {
        final int numberOfUsers = 5;

        for (int i = 0; i < numberOfUsers; i++) {
            createUser("user_" + i, PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE);
        }

        final ValidatableResponse response = given(documentationSpec)
                .filter(document("list_all_users"))
                .log().all()
                .auth().basic("user_" + (numberOfUsers - 1), PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath())
                .then()
                .statusCode(HttpStatus.OK.value());

        response.assertThat().header("X-Total-Count", String.valueOf(numberOfUsers));
        response.assertThat().body("size()", is(numberOfUsers));
    }

    @Test
    public void shouldFindUsersByUsername() {
        //create admin
        createUser(USERNAME_ADMIN, PASSWORD, UserType.ADMIN.name(), NAME, LAST_NAME, EMAIL, AGE);
        //create moderator
        createUser(USERNAME_MODERATOR, PASSWORD, UserType.MODERATOR.name(), NAME, LAST_NAME, EMAIL, AGE);

        //create users
        createUser("user_1", PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE);
        createUser("user_test", PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE);
        createUser("test_user", PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE);

        final ValidatableResponse response = given(documentationSpec)
                .filter(document("find_users_by_username"))
                .log().all()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "?username=user")
                .then()
                .statusCode(HttpStatus.OK.value());

        response.assertThat().header("X-Total-Count", "3");
        response.assertThat().body("size()", is(3));
    }

    @Test
    public void shouldFindUsersByUsernameOnPage() {
        //create admin
        createUser(USERNAME_ADMIN, PASSWORD, UserType.ADMIN.name(), NAME, LAST_NAME, EMAIL, AGE);
        //create moderator
        createUser(USERNAME_MODERATOR, PASSWORD, UserType.MODERATOR.name(), NAME, LAST_NAME, EMAIL, AGE);

        //create users
        createUser("user_1", PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE);
        createUser("user_test", PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE);
        createUser("test_user", PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE);

        final ValidatableResponse response = given(documentationSpec)
                .filter(document("find_users_by_username"))
                .log().all()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "?username=user&page=1&size=2")
                .then()
                .statusCode(HttpStatus.OK.value());

        response.assertThat().header("X-Total-Count", "3");
        response.assertThat().body("size()", is(1));
    }

    @Test
    public void shouldGetUserById() {
        final UUID userId = createUser(USERNAME_USER, PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE)
                .extract().body().jsonPath().getUUID("user_id");

        final ValidatableResponse response = given(documentationSpec)
                .filter(document("get_user_by_id"))
                .log().all()
                .auth().basic(USERNAME_USER, PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "/" + userId.toString())
                .then()
                .statusCode(HttpStatus.OK.value());

        response.assertThat().body("user_id", is(userId.toString()));
        response.assertThat().body("type", is(UserType.USER.name()));
        response.assertThat().body("username", is(USERNAME_USER));
        response.assertThat().body("name", is(NAME));
        response.assertThat().body("last_name", is(LAST_NAME));
        response.assertThat().body("email", is(EMAIL));
        response.assertThat().body("age", is(AGE));
    }

    @Test
    public void shouldUpdateUser() {
        //create admin
        createUser(USERNAME_ADMIN, PASSWORD, UserType.ADMIN.name(), NAME, LAST_NAME, EMAIL, AGE);

        final UUID userId = createUser(USERNAME_USER, PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE)
                .extract().body().jsonPath().getUUID("user_id");

        given(documentationSpec)
                .filter(document("update_user"))
                .log().all()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ImmutableMap.builder()
                        .put("password", "updated_pass")
                        .put("type", UserType.MODERATOR.name())
                        .put("name", "updated_name")
                        .put("last_name", "updated_last_name")
                        .put("age", 25)
                        .build())
                .when()
                .request(HttpMethod.PUT.name(), getPath() + "/" + userId.toString())
                .then()
                .statusCode(HttpStatus.OK.value());

        final ValidatableResponse response = given()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "/" + userId.toString())
                .then()
                .statusCode(HttpStatus.OK.value());

        response.assertThat().body("user_id", is(userId.toString()));
        response.assertThat().body("type", is(UserType.MODERATOR.name()));
        response.assertThat().body("name", is("updated_name"));
        response.assertThat().body("last_name", is("updated_last_name"));
        response.assertThat().body("age", is(25));
    }

    @Test
    public void shouldDeleteUser() {
        //create admin
        createUser(USERNAME_ADMIN, PASSWORD, UserType.ADMIN.name(), NAME, LAST_NAME, EMAIL, AGE);

        final UUID userId = createUser(USERNAME_USER, PASSWORD, UserType.USER.name(), NAME, LAST_NAME, EMAIL, AGE)
                .extract().body().jsonPath().getUUID("user_id");

        given(documentationSpec)
                .filter(document("delete_user"))
                .log().all()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.DELETE.name(), getPath() + "/" + userId.toString())
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "/" + userId.toString())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Override
    protected String getPath() {
        return uri + USER_API_PATH;
    }
}

