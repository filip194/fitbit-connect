package hr.fitbit.demo.fitbitconnect.apimodel.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hr.fitbit.demo.fitbitconnect.util.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "User register information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegister implements Serializable {

    private static final long serialVersionUID = 5313493413859894496L;

    @NotNull
    @Size(min = 1, max = 50)
    @Nationalized
    @ApiModelProperty(value = "username", position = 1, example = "username")
    private String username;

    @NotNull
    @Size(min = 1, max = 50)
    @Nationalized
    @ApiModelProperty(value = "password", position = 2, example = "password")
    private String password;

    @NotNull
    @ApiModelProperty(value = "type", position = 3, example = "USER")
    private UserType type;

    @Size(min = 1, max = 255)
    @Nationalized
    @ApiModelProperty(value = "name", position = 4, example = "name")
    private String name;

    @Size(min = 1, max = 255)
    @Nationalized
    @ApiModelProperty(value = "last name", position = 5, example = "lastname")
    private String lastName;

    @NotNull
    @Size(min = 1, max = 255)
    @Nationalized
    @ApiModelProperty(value = "email", position = 6, example = "test@test.com")
    private String email;

    @Min(1)
    @Max(130)
    @ApiModelProperty(value = "age", position = 7, example = "24")
    private Integer age;
}

