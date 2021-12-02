package hr.fitbit.demo.fitbitconnect.apimodel.fitbit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "OAuth2 token information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse implements Serializable {

    private static final long serialVersionUID = 5313493413859894498L;

    @Schema(name = "access token")
    private String accessToken;

    @Schema(name = "refresh token")
    private String refreshToken;

    @Schema(name = "token type", example = "Bearer")
    private String tokenType;

    @Schema(name = "fibit user id", example = "ABCDE1")
    private String userId;

    @Schema(name = "expires in", example = "28800")
    private Integer expiresIn;

    @Schema(name = "scope of user tracking", example = "activity sleep")
    private String scope;

}
