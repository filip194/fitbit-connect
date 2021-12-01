package hr.fitbit.demo.fitbitconnect.apimodel.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import hr.fitbit.demo.fitbitconnect.util.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(title = "User update information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdate implements Serializable {

    private static final long serialVersionUID = 5313493413859894493L;

    @Size(min = 1, max = 50)
    @Nationalized
    @Schema(name = "password", example = "password")
    private String password;

    @Schema(name = "type", example = "USER")
    private UserType type;

    @Size(min = 1, max = 255)
    @Nationalized
    @Schema(name = "name", example = "name")
    private String name;

    @Size(min = 1, max = 255)
    @Nationalized
    @Schema(name = "last name", example = "lastname")
    private String lastName;

    @Min(1)
    @Max(130)
    @Schema(name = "age", example = "24")
    private Integer age;
}

