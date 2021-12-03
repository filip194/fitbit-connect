package hr.fitbit.demo.fitbitconnect.apisupport.apimodel.fitbit;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {

    @Schema(name = "active score", example = "-1")
    protected Integer activeScore;

    @Schema(name = "calories out", example = "-1")
    protected Integer caloriesOut;

    @Schema(name = "distance", example = "0")
    protected Integer distance;

    @Schema(name = "steps", example = "0")
    protected Integer steps;
}

