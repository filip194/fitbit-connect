package hr.fitbit.demo.fitbitconnect.apimodel.error;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ApiModel("Error information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error implements Serializable {

    private String error;
}

