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
@ApiModel(value = "User information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 5313493413859894497L;

    @ApiModelProperty(value = "user id", position = 1, example = "80264b81-3048-45c8-bc02-5c63a2372d3e")
    private UUID userId;

    @ApiModelProperty(value = "type", position = 2, example = "USER")
    private UserType type;

    @ApiModelProperty(value = "username", position = 3, example = "username")
    private String username;

    @ApiModelProperty(value = "name", position = 5, example = "name")
    private String name;

    @ApiModelProperty(value = "last name", position = 6, example = "lastname")
    private String lastName;

    @ApiModelProperty(value = "email", position = 7, example = "test@test.com")
    private String email;

    @ApiModelProperty(value = "age", position = 8, example = "24")
    private Integer age;

}

