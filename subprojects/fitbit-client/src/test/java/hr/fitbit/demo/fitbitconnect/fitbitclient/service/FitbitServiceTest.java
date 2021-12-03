package hr.fitbit.demo.fitbitconnect.fitbitclient.service;

import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.fitbit.TokenResponse;
import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.fitbit.UserActivityResponse;
import hr.fitbit.demo.fitbitconnect.fitbitclient.FitbitClient;
import hr.fitbit.demo.fitbitconnect.dao.entity.RoleEntity;
import hr.fitbit.demo.fitbitconnect.dao.entity.UserEntity;
import hr.fitbit.demo.fitbitconnect.testsupport.fixture.FitbitFixture;
import hr.fitbit.demo.fitbitconnect.dao.fixture.RoleFixture;
import hr.fitbit.demo.fitbitconnect.dao.fixture.UserFixture;
import hr.fitbit.demo.fitbitconnect.dao.repository.RoleRepository;
import hr.fitbit.demo.fitbitconnect.dao.repository.UserRepository;
import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
public class FitbitServiceTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FitbitService fitbitService;

    @MockBean
    private FitbitClient fitbitClient;

    @BeforeEach
    @AfterEach
    public void cleanDatabase() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    @Transactional
    public void shouldRegisterUserOnFitbit() {
        final String username = "username";
        final UserType type = UserType.USER;
        final TokenResponse tokenResponse = FitbitFixture.createFitbitTokenResponse();

        when(fitbitClient.requestToken(any())).thenReturn(new ResponseEntity<>(tokenResponse, HttpStatus.OK));

        final RoleEntity roleEntity = RoleFixture.createRoleEntity(type.name());
        roleRepository.save(roleEntity);

        final UserEntity userEntity = UserFixture.createUserEntity(
                username,
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                type,
                Collections.singleton(roleEntity)
        );
        userRepository.save(userEntity);

        roleEntity.addUser(userEntity);
        roleRepository.save(roleEntity);

        // register user
        fitbitService.registerUserOnFitbit(username);

        final UserEntity entity = userRepository.findAll().get(0);

        assertThat(entity.getFUserId()).isEqualTo("ABCDE1");
        assertThat(entity.getFAccessToken()).isEqualTo("token_12345");
        assertThat(entity.getFRefreshToken()).isEqualTo("refresh_token_12345");
    }

    @Test
    @Transactional
    public void shouldGetRefreshToken() {
        final String username = "username";
        final UserType type = UserType.USER;
        final RoleEntity roleEntity = RoleFixture.createRoleEntity(type.name());
        roleRepository.save(roleEntity);

        final UserEntity userEntity = UserFixture.createUserEntity(
                username,
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                type,
                Collections.singleton(roleEntity)
        );

        userEntity.setFAccessToken("test_token");
        userEntity.setFRefreshToken("test_refresh_token");
        userEntity.setFUserId("ABCDE1");
        userRepository.save(userEntity);

        roleEntity.addUser(userEntity);
        roleRepository.save(roleEntity);

        final UserEntity entity = userRepository.findAll().get(0);

        // refresh
        final TokenResponse tokenResponseRefreshed = FitbitFixture.createFitbitTokenResponseRefreshed();
        when(fitbitClient.refreshUserToken(anyString(), anyInt())).thenReturn(new ResponseEntity<>(tokenResponseRefreshed, HttpStatus.OK));

        final TokenResponse refreshTokenResponse = fitbitService.getRefreshToken(entity.getUserId());

        final UserEntity entityRefreshed = userRepository.findAll().get(0);

        assertThat(entityRefreshed.getFUserId()).isEqualTo(refreshTokenResponse.getUserId());
        assertThat(entityRefreshed.getFAccessToken()).isEqualTo(refreshTokenResponse.getAccessToken());
        assertThat(entityRefreshed.getFRefreshToken()).isEqualTo(refreshTokenResponse.getRefreshToken());
    }

    @Test
    @Transactional
    public void shouldGetUserActivities() {
        final String username = "username";
        final UserType type = UserType.USER;
        final RoleEntity roleEntity = RoleFixture.createRoleEntity(type.name());
        roleRepository.save(roleEntity);

        final UserEntity userEntity = UserFixture.createUserEntity(
                username,
                "password",
                "email@test.com",
                25,
                "name",
                "last_name",
                type,
                Collections.singleton(roleEntity)
        );

        userEntity.setFAccessToken("test_token");
        userEntity.setFRefreshToken("test_refresh_token");
        userEntity.setFUserId("ABCDE1");
        userRepository.save(userEntity);

        roleEntity.addUser(userEntity);
        roleRepository.save(roleEntity);

        final UserEntity entity = userRepository.findAll().get(0);

        // get activities
        final UserActivityResponse response = FitbitFixture.createFitbitActivityResponse();
        when(fitbitClient.getUserActivities(any(), anyString())).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        final UserActivityResponse activityResponse = fitbitService.getUserActivities(entity.getUserId());

        assertThat(activityResponse.getLifetime().getTracker().getActiveScore()).isEqualTo(5);
        assertThat(activityResponse.getLifetime().getTracker().getSteps()).isEqualTo(6000);
        assertThat(activityResponse.getLifetime().getTracker().getDistance()).isEqualTo(4000);
        assertThat(activityResponse.getLifetime().getTracker().getCaloriesOut()).isEqualTo(500);

        assertThat(activityResponse.getLifetime().getTotal().getActiveScore()).isEqualTo(10);
        assertThat(activityResponse.getLifetime().getTotal().getSteps()).isEqualTo(120000);
        assertThat(activityResponse.getLifetime().getTotal().getDistance()).isEqualTo(80000);
        assertThat(activityResponse.getLifetime().getTotal().getCaloriesOut()).isEqualTo(100000);
    }

}

