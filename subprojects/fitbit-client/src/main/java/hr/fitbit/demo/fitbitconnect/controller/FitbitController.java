package hr.fitbit.demo.fitbitconnect.controller;

import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.TokenResponse;
import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.UserActivityResponse;
import hr.fitbit.demo.fitbitconnect.security.UserRole;
import hr.fitbit.demo.fitbitconnect.service.FitbitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/fitbit")
@Api(tags = "fitbit")
public class FitbitController extends ExceptionHandlerController {

    private final FitbitService fitbitService;

    public FitbitController(FitbitService fitbitService) {
        this.fitbitService = fitbitService;
    }

    @Secured(UserRole.AUTHENTICATED_USER_ROLE)
    @GetMapping("/redirect")
    @ApiOperation(value = "Get user code from official Fitbit site")
    public ResponseEntity<String> registerUserOnFitbit(HttpServletRequest request, @RequestParam(name = "code") String code) {
        final String userCode = code.replace("#_=_", "");
        log.info("User code={}", userCode);
        final String user = request.getUserPrincipal().getName();
        log.info("User={}", user);

        fitbitService.setCode(code);
        final boolean registered = fitbitService.registerUserOnFitbit(user);
        if (registered) {
            final String responseText = "<h2>Congratulations " + user + "!</h2><h3>Your account is now connected to Fitbit.</h3>";
            return new ResponseEntity<>(responseText, HttpStatus.OK);
        } else {
            final String responseText = "<h2>User not found</h2>";
            return new ResponseEntity<>(responseText, HttpStatus.NOT_FOUND);
        }
    }

    @Secured(UserRole.ADMIN_ROLE)
    @GetMapping(value = "/refresh-token/{user_id}")
    @ApiOperation(value = "Refresh user token")
    public ResponseEntity<TokenResponse> refreshUserAccessToken(@PathVariable("user_id") @ApiParam(value = "user_id", required = true) UUID userId) {
        log.info("Refresh token for user={}", userId);

        final TokenResponse response = fitbitService.getRefreshToken(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Secured(UserRole.ADMIN_ROLE)
    @GetMapping(value = "/activities/{user_id}")
    @ApiOperation(value = "Get user activities")
    public ResponseEntity<UserActivityResponse> getUserActivities(@PathVariable("user_id") @ApiParam(value = "user_id", required = true) UUID userId) {
        log.info("Get activities for user={}", userId);

        final UserActivityResponse response = fitbitService.getUserActivities(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

