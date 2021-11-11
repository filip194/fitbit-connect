package hr.fitbit.demo.fitbitconnect.apimodel.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hr.fitbit.demo.fitbitconnect.util.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("User created response information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 5313493413859894400L;

    @ApiModelProperty(value = "user id", position = 1, example = "80264b81-3048-45c8-bc02-5c63a2372d3e")
    private UUID userId;

    @ApiModelProperty(value = "username", position = 2, example = "username")
    private String username;

    @ApiModelProperty(value = "type", position = 3, example = "USER")
    private UserType type;

}

