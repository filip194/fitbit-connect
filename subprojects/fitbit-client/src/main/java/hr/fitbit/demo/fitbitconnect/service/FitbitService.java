package hr.fitbit.demo.fitbitconnect.service;

import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.TokenResponse;
import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.UserActivityResponse;
import hr.fitbit.demo.fitbitconnect.client.FitbitClient;
import hr.fitbit.demo.fitbitconnect.entity.UserEntity;
import hr.fitbit.demo.fitbitconnect.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;
import java.util.UUID;

@Service
public class FitbitService {

    private static final Logger LOG = LoggerFactory.getLogger(FitbitService.class);

    private static final int DEFAULT_TOKEN_EXPIRATION_TIME_IN_SECONDS = 28800; // 8 hours

    private final FitbitClient fitbitClient;
    private final UserRepository userRepository;

    @Getter
    @Setter
    private String code;

    public FitbitService(FitbitClient fitbitClient, UserRepository userRepository) {
        this.fitbitClient = fitbitClient;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean registerUserOnFitbit(String username) {
        final ResponseEntity<TokenResponse> responseEntity = fitbitClient.requestToken(code);
        LOG.info("CODE={}", code);
        checkIfStatusCodeOK(responseEntity);

        final TokenResponse response = responseEntity.getBody();
        final Optional<UserEntity> userEntity = userRepository.findByUsername(username);

        if (userEntity.isEmpty()) {
            return false;
        }

        userEntity.get().setFAccessToken(response.getAccessToken());
        userEntity.get().setFRefreshToken(response.getRefreshToken());
        userEntity.get().setFUserId(response.getUserId());
        userRepository.saveAndFlush(userEntity.get());
        return true;
    }

    @Transactional
    public TokenResponse getRefreshToken(UUID userId) {
        final UserEntity userEntity = checkIfUserExists(userId);
        checkIfUserRegisteredOnFitbit(userEntity);

        final String refreshToken = userEntity.getFRefreshToken();
        final ResponseEntity<TokenResponse> responseEntity = fitbitClient.refreshUserToken(refreshToken,
                DEFAULT_TOKEN_EXPIRATION_TIME_IN_SECONDS);
        checkIfStatusCodeOK(responseEntity);
        final TokenResponse response = responseEntity.getBody();

        userEntity.setFAccessToken(response.getAccessToken());
        userEntity.setFRefreshToken(response.getRefreshToken());

        if (!userEntity.getFUserId().equals(response.getUserId())) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Fitbit user id in database and the user id in response do not match");
        }

        userRepository.saveAndFlush(userEntity);
        return response;
    }

    @Transactional
    public UserActivityResponse getUserActivities(UUID userId) {
        final UserEntity userEntity = checkIfUserExists(userId);
        checkIfUserRegisteredOnFitbit(userEntity);

        ResponseEntity<UserActivityResponse> responseEntity = null;

        try {
            responseEntity = fitbitClient.getUserActivities(userEntity.getFUserId(), userEntity.getFAccessToken());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                getRefreshToken(userId);
            } else {
                throw new HttpClientErrorException(responseEntity.getStatusCode(),
                        "Status code was=" + responseEntity.getStatusCode());
            }
        }

        responseEntity = fitbitClient
                .getUserActivities(userEntity.getFUserId(), userEntity.getFAccessToken());

        checkIfStatusCodeOK(responseEntity);
        return responseEntity.getBody();
    }

    private UserEntity checkIfUserExists(UUID userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void checkIfUserRegisteredOnFitbit(UserEntity userEntity) {
        if (userEntity.getFUserId() == null || userEntity.getFUserId().equals("")) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User is not registered on Fitbit");
        }
    }

    private void checkIfStatusCodeOK(ResponseEntity response) {
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpClientErrorException(response.getStatusCode(), "Status code was=" + response.getStatusCode());
        }
    }

}

