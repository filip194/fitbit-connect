package hr.fitbit.demo.fitbitconnect.apimodel.fitbit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("OAuth2 token information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse implements Serializable {

    private static final long serialVersionUID = 5313493413859894498L;

    @ApiModelProperty(value = "access token", position = 1)
    private String accessToken;

    @ApiModelProperty(value = "refresh token", position = 2)
    private String refreshToken;

    @ApiModelProperty(value = "token type", position = 3, example = "Bearer")
    private String tokenType;

    @ApiModelProperty(value = "fibit user id", position = 4, example = "ABCDE1")
    private String userId;

    @ApiModelProperty(value = "expires in", position = 5, example = "28800")
    private Integer expiresIn;

    @ApiModelProperty(value = "scope of user tracking", position = 6, example = "activity sleep")
    private String scope;

}
