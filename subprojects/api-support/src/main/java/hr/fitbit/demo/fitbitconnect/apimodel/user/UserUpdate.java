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
import javax.validation.constraints.Size;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel("User update information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdate implements Serializable {

    @Size(min = 1, max = 50)
    @Nationalized
    @ApiModelProperty(value = "password", position = 1, example = "password")
    private String password;

    @ApiModelProperty(value = "type", position = 2, example = "USER")
    private UserType type;

    @Size(min = 1, max = 255)
    @Nationalized
    @ApiModelProperty(value = "name", position = 3, example = "name")
    private String name;

    @Size(min = 1, max = 255)
    @Nationalized
    @ApiModelProperty(value = "last name", position = 4, example = "lastname")
    private String lastName;

    @Min(1)
    @Max(130)
    @ApiModelProperty(value = "age", position = 5, example = "24")
    private Integer age;
}

