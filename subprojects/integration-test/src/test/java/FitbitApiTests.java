import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.TokenResponse;
import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.UserActivityResponse;
import hr.fitbit.demo.fitbitconnect.client.FitbitClient;
import hr.fitbit.demo.fitbitconnect.fixture.FitbitFixture;
import hr.fitbit.demo.fitbitconnect.fixture.TokenFixture;
import hr.fitbit.demo.fitbitconnect.util.UserType;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;

public class FitbitApiTests extends TestSupport {

    private static final String FITBIT_API_PATH = "/api/fitbit";

    @Autowired
    protected ObjectMapper mapper;

    protected RestTemplate restTemplate;

    @Autowired
    private FitbitClient fitbitClient;

    @Value("${wiremock.server.port}")
    private int port;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        createRoles();
        restTemplate = mock(RestTemplate.class);
    }

    @Test
    public void shouldRegisterUserOnFitbit() throws JsonProcessingException {
        //create admin
        createUser(USERNAME_ADMIN, PASSWORD, UserType.ADMIN.name(), NAME, LAST_NAME, EMAIL, AGE);

        final TokenResponse tokenResponse = FitbitFixture.createFitbitTokenResponse();
        final String tokenResponseString = mapper.writeValueAsString(tokenResponse);

        TokenFixture.createTokenStub(tokenResponseString, wiremockPort);

        final ValidatableResponse response = given(documentationSpec)
                .filter(document("register_user_on_fitbit"))
                .log().all()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "/redirect?code=test_code")
                .then()
                .statusCode(HttpStatus.OK.value());

        final String responseBody = response.assertThat().extract().body().asString();
        assertThat(responseBody).isEqualTo("<h2>Congratulations admin!</h2><h3>Your account is now connected to Fitbit.</h3>");

    }

    @Test
    public void shouldRefreshToken() throws JsonProcessingException {
        //create admin
        final UUID userId = createUser(USERNAME_ADMIN, PASSWORD, UserType.ADMIN.name(), NAME, LAST_NAME, EMAIL, AGE)
                .extract().body().jsonPath().getUUID("user_id");

        final TokenResponse tokenResp = FitbitFixture.createFitbitTokenResponse();
        final String tokenRespString = mapper.writeValueAsString(tokenResp);

        TokenFixture.createTokenStub(tokenRespString, wiremockPort);

        given()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "/redirect?code=test_code")
                .then()
                .statusCode(HttpStatus.OK.value());

        final TokenResponse tokenResponse = FitbitFixture.createFitbitTokenResponseRefreshed();
        final String tokenResponseString = mapper.writeValueAsString(tokenResponse);

        TokenFixture.createTokenStub(tokenResponseString, wiremockPort);

        final ValidatableResponse response = given(documentationSpec)
                .filter(document("refresh_fitbit_token"))
                .log().all()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "/refresh-token/" + userId.toString())
                .then()
                .statusCode(HttpStatus.OK.value());

        response.assertThat().body("access_token", is("token_12345_refreshed"));
        response.assertThat().body("refresh_token", is("refresh_token_12345_new"));
        response.assertThat().body("token_type", is("Bearer"));
        response.assertThat().body("user_id", is("ABCDE1"));
        response.assertThat().body("expires_in", is(28800));
        response.assertThat().body("scope", is("heartrate sleep profile social settings location activity nutrition weight"));
    }

    @Test
    public void shouldGetUserActivities() throws JsonProcessingException {
        //create admin
        final UUID userId = createUser(USERNAME_ADMIN, PASSWORD, UserType.ADMIN.name(), NAME, LAST_NAME, EMAIL, AGE)
                .extract().body().jsonPath().getUUID("user_id");

        final TokenResponse tokenResp = FitbitFixture.createFitbitTokenResponse();
        final String tokenRespString = mapper.writeValueAsString(tokenResp);

        TokenFixture.createTokenStub(tokenRespString, wiremockPort);

        given()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "/redirect?code=test_code")
                .then()
                .statusCode(HttpStatus.OK.value());

        final UserActivityResponse activityResponse = FitbitFixture.createFitbitActivityResponse();
        final String activityResponseString = mapper.writeValueAsString(activityResponse);

        TokenFixture.createUserActivitiesStub(activityResponseString, tokenResp.getUserId(), tokenResp.getAccessToken());

        final ValidatableResponse response = given(documentationSpec)
                .filter(document("get_user_activities"))
                .log().all()
                .auth().basic(USERNAME_ADMIN, PASSWORD)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(HttpMethod.GET.name(), getPath() + "/activities/" + userId.toString())
                .then()
                .statusCode(HttpStatus.OK.value());

        response.assertThat().body("lifetime.tracker.activeScore", is(5));
        response.assertThat().body("lifetime.tracker.caloriesOut", is(500));
        response.assertThat().body("lifetime.tracker.steps", is(6000));
        response.assertThat().body("lifetime.tracker.distance", is(4000));

        response.assertThat().body("lifetime.total.activeScore", is(10));
        response.assertThat().body("lifetime.total.caloriesOut", is(100000));
        response.assertThat().body("lifetime.total.steps", is(120000));
        response.assertThat().body("lifetime.total.distance", is(80000));

    }

    @Override
    protected String getPath() {
        return uri + FITBIT_API_PATH;
    }
}

