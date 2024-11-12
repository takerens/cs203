package csd.grp3.CheaterBugAPI;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

public class CheaterbugEntity {


    // Request fields
    @NotNull(message = "Expected score must not be null")
    @DecimalMin(value = "0.0", message = "Expected score must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Expected score must be between 0.0 and 1.0")
    private Double expectedScore;

    @NotNull(message = "Actual score must not be null")
    @DecimalMin(value = "0.0", message = "Actual score must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Actual score must be between 0.0 and 1.0")
    private Double actualScore;

    public CheaterbugEntity(Double expectedScore, Double actualScore) {
        this.expectedScore = expectedScore;
        this.actualScore = actualScore;
    }   

}
