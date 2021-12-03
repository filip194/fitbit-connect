package hr.fitbit.demo.fitbitconnect.testsupport.fixture;

import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TokenFixture {

    private static final String TOKEN_PATH = "/oauth2/token";
    private static final String USERNAME = "test_username";
    private static final String PASSWORD = "test_password";
    private static final String REDIRECT_PREFIX = "http://localhost:";
    private static final String REDIRECT_SUFFIX = "/api/fitbit/redirect";
    private static final String ACTIVITIES_PREFIX = "/1/user/";
    private static final String ACTIVITIES_SUFFIX = "/activities.json";

    public static void createTokenStub(String responseBody, int port) {
        final String requestBody = String.format(
                "client_id=%s&grant_type=authorization_code&redirect_uri=%s&code=test_code",
                USERNAME,
                REDIRECT_PREFIX + port + REDIRECT_SUFFIX
        );

        stubFor(post(urlEqualTo(TOKEN_PATH))
                .withBasicAuth(USERNAME, PASSWORD)
                .withHeader("Content-Type", equalTo(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8"))
//                encodes ':' to '%3A'
//                .withRequestBody(equalTo(requestBody))
                .willReturn(
                        aResponse()
                                .withBody(responseBody)
                                .withStatus(200)
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
    }

    public static void createUserActivitiesStub(String responseBody, String fitbitUserId, String token) {

        stubFor(get(urlEqualTo(ACTIVITIES_PREFIX + fitbitUserId + ACTIVITIES_SUFFIX))
                .withHeader("Authorization", equalTo("Bearer " + token))
                .willReturn(
                        aResponse()
                                .withBody(responseBody)
                                .withStatus(200)
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));
    }
}

