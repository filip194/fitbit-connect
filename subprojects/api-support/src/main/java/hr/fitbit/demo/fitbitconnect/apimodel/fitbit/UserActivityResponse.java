package hr.fitbit.demo.fitbitconnect.apimodel.fitbit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonNaming
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("User activity information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityResponse implements Serializable {

    @ApiModelProperty(value = "lifetime", position = 1, example = "" +
            "{\n" +
            "    \"total\": {\n" +
            "      \"activeScore\": -1,\n" +
            "      \"caloriesOut\": -1,\n" +
            "      \"distance\": 0,\n" +
            "      \"steps\": 0\n" +
            "    },\n" +
            "    \"tracker\": {\n" +
            "      \"activeScore\": -1,\n" +
            "      \"caloriesOut\": -1,\n" +
            "      \"distance\": 0,\n" +
            "      \"steps\": 0\n" +
            "    }\n" +
            "  }")
    private Lifetime lifetime;

}
