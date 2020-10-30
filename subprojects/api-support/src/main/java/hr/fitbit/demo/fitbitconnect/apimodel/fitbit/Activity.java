package hr.fitbit.demo.fitbitconnect.apimodel.fitbit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @ApiModelProperty(value = "active score", position = 1, example = "-1")
    protected Integer activeScore;

    @ApiModelProperty(value = "calories out", position = 2, example = "-1")
    protected Integer caloriesOut;

    @ApiModelProperty(value = "distance", position = 3, example = "0")
    protected Integer distance;

    @ApiModelProperty(value = "steps", position = 4, example = "0")
    protected Integer steps;
}

