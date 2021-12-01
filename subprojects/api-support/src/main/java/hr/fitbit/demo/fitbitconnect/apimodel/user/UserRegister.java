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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(title = "User register information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegister implements Serializable {

    private static final long serialVersionUID = 5313493413859894496L;

    @NotNull
    @Size(min = 1, max = 50)
    @Nationalized
    @Schema(name = "username", example = "username")
    private String username;

    @NotNull
    @Size(min = 1, max = 50)
    @Nationalized
    @Schema(name = "password", example = "password")
    private String password;

    @NotNull
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

    @NotNull
    @Size(min = 1, max = 255)
    @Nationalized
    @Schema(name = "email", example = "test@test.com")
    private String email;

    @Min(1)
    @Max(130)
    @Schema(name = "age", example = "24")
    private Integer age;
}

