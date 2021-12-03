package hr.fitbit.demo.fitbitconnect.apisupport.apimodel.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "User information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 5313493413859894497L;

    @Schema(name = "user id", example = "80264b81-3048-45c8-bc02-5c63a2372d3e")
    private UUID userId;

    @Schema(name = "type", example = "USER")
    private UserType type;

    @Schema(name = "username", example = "username")
    private String username;

    @Schema(name = "name", example = "name")
    private String name;

    @Schema(name = "last name", example = "lastname")
    private String lastName;

    @Schema(name = "email", example = "test@test.com")
    private String email;

    @Schema(name = "age", example = "24")
    private Integer age;

}

