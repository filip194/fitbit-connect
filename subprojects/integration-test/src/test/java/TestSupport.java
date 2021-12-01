import javax.annotation.PostConstruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;

import hr.fitbit.demo.fitbitconnect.TestApplication;
import hr.fitbit.demo.fitbitconnect.entity.RoleEntity;
import hr.fitbit.demo.fitbitconnect.fixture.RoleFixture;
import hr.fitbit.demo.fitbitconnect.repository.RoleRepository;
import hr.fitbit.demo.fitbitconnect.repository.UserRepository;
import hr.fitbit.demo.fitbitconnect.util.UserType;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import wiremock.com.google.common.collect.ImmutableMap;

import static io.restassured.RestAssured.given;

@Slf4j
@ContextConfiguration(classes = TestApplication.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureTestDatabase
@AutoConfigureRestDocs
@AutoConfigureWireMock(port = 23456)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "server.port=2345",
                "fitbit.client.id=test_username",
                "fitbit.client.secret=test_password",
                "fitbit.authorization.uri=http://localhost:23456/oauth2/authorize",
                "fitbit.base.api.uri=http://localhost:23456",
                "app.release.version=0.0",
                "app.build.version=FITBIT.TEST"})
public abstract class TestSupport {

    protected final static String USERNAME_ADMIN = "admin";
    protected final static String USERNAME_MODERATOR = "moderator";
    protected final static String USERNAME_USER = "test_user";
    protected final static String PASSWORD = "pass";
    protected final static String NAME = "name";
    protected final static String LAST_NAME = "last_name";
    protected final static String EMAIL = "test@test.com";
    protected final static int AGE = 30;

    @Value("${wiremock.server.port}")
    protected int wiremockPort;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected UserRepository userRepository;

    protected String uri;
    protected RequestSpecification documentationSpec;

    protected abstract String getPath();

    @LocalServerPort
    private int port;

    @PostConstruct
    private void initUri() {
        uri = "http://localhost:" + port;
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        documentationSpec = new RequestSpecBuilder()
                .addFilter(RestAssuredRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
        WireMock.reset();
    }

    protected ValidatableResponse createUser(
            String username, String password, String userType, String name, String lastName, String email, int age) {

        return given()
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ImmutableMap.builder()
                        .put("username", username)
                        .put("password", password)
                        .put("type", userType)
                        .put("name", name)
                        .put("last_name", lastName)
                        .put("email", email)
                        .put("age", age)
                        .build())
                .when()
                .request(HttpMethod.POST.name(), uri + "/api/users/register")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Transactional
    protected void createRoles() {
        final RoleEntity roleUser = RoleFixture.createRoleEntity(UserType.USER.name());
        final RoleEntity roleModerator = RoleFixture.createRoleEntity(UserType.MODERATOR.name());
        final RoleEntity roleAdmin = RoleFixture.createRoleEntity(UserType.ADMIN.name());
        roleRepository.save(roleUser);
        roleRepository.save(roleModerator);
        roleRepository.save(roleAdmin);
    }

}
