package csd.grp3.CheaterBugAPI;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheaterbugEntity {

    private static final double CLASSINTERVAL = 50.0;

    @NotNull(message = "User ELO must not be null")
    private Double userELO;

    @NotNull(message = "Opponent ELO must not be null")
    private Double opponentELO;

    @NotNull(message = "Actual score must not be null")
    @DecimalMin(value = "0.0", message = "Actual score must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Actual score must be between 0.0 and 1.0")
    private Double actualScore;

    @DecimalMin(value = "0.0", message = "Expected score must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", message = "Expected score must be between 0.0 and 1.0")
    private Double expectedScore;

    public CheaterbugEntity(Double userELO, Double opponentELO, Double actualScore) {
        this.userELO = userELO;
        this.opponentELO = opponentELO;
        this.actualScore = actualScore;
        this.expectedScore = calculateExpectedScore(userELO, opponentELO, CLASSINTERVAL);
    }

    // Method to calculate expected score based on userELO, opponentELO, and classInterval
    private double calculateExpectedScore(double userELO, double opponentELO, double classInterval) {
        return 1.0 / (1 + Math.pow(10, (opponentELO - userELO) / classInterval));
    }
}
