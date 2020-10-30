package hr.fitbit.demo.fitbitconnect.fixture;

import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.Activity;
import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.Lifetime;
import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.TokenResponse;
import hr.fitbit.demo.fitbitconnect.apimodel.fitbit.UserActivityResponse;

public class FitbitFixture {

    public static TokenResponse createFitbitTokenResponse() {

        final TokenResponse response = new TokenResponse();
        response.setUserId("ABCDE1");
        response.setExpiresIn(28800);
        response.setScope(createScope());
        response.setAccessToken("token_12345");
        response.setRefreshToken("refresh_token_12345");
        response.setTokenType("Bearer");
        return response;
    }

    public static TokenResponse createFitbitTokenResponseRefreshed() {
        final TokenResponse response = new TokenResponse();
        response.setUserId("ABCDE1");
        response.setExpiresIn(28800);
        response.setScope(createScope());
        response.setAccessToken("token_12345_refreshed");
        response.setRefreshToken("refresh_token_12345_new");
        response.setTokenType("Bearer");
        return response;
    }

    public static UserActivityResponse createFitbitActivityResponse() {

        final Activity tracker = new Activity();
        tracker.setSteps(6000);
        tracker.setDistance(4000);
        tracker.setActiveScore(5);
        tracker.setCaloriesOut(500);

        final Activity total = new Activity();
        total.setSteps(120000);
        total.setDistance(80000);
        total.setActiveScore(10);
        total.setCaloriesOut(100000);

        final Lifetime lifetime = new Lifetime();
        lifetime.setTracker(tracker);
        lifetime.setTotal(total);

        final UserActivityResponse response = new UserActivityResponse();
        response.setLifetime(lifetime);
        return response;
    }

    private static String createScope() {
        final StringBuilder scope = new StringBuilder(9);
        scope.append("heartrate ");
        scope.append("sleep ");
        scope.append("profile ");
        scope.append("social ");
        scope.append("settings ");
        scope.append("location ");
        scope.append("activity ");
        scope.append("nutrition ");
        scope.append("weight");
        return scope.toString();
    }
}

