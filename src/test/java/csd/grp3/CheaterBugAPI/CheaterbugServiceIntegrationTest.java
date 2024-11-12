package csd.grp3.CheaterBugAPI;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CheaterbugServiceIntegrationTest {

    @Autowired
    private CheaterbugService cheaterbugService;

    @Test
    public void testAnalyzeWithRealApiCall() {
        // Prepare the input data (a list of CheaterbugEntity objects)
        CheaterbugEntity entity1 = new CheaterbugEntity(1200, 1800, 0.5);
        CheaterbugEntity entity2 = new CheaterbugEntity(1200, 1100, 1.0);
        List<CheaterbugEntity> requestPayload = List.of(entity1, entity2);

        // Call the service method
        CheaterbugResponse response = cheaterbugService.analyze(requestPayload);

        // Print the response to see actual values
        System.out.println("Cheat Probability: " + response.getCheatProbability());
        System.out.println("Expected Probability: " + response.getExpectedProbability());

        // Assertions to verify the response
        assertNotNull(response, "The response should not be null");
        assertNotNull(response.getCheatProbability(), "Cheat probability should not be null");
        assertNotNull(response.getExpectedProbability(), "Expected probability should not be null");

        // Check specific values if known or expected structure
        assertTrue(response.getCheatProbability().containsKey("99thPercentile"));
        assertTrue(response.getExpectedProbability().containsKey("5thPercentile"));
    }

    @Test
    public void testAnalyze_NormalPlay_NoSuspicion() {
        // Prepare normal play data
        CheaterbugEntity entity1 = new CheaterbugEntity(1500, 1600, 0.5);
        CheaterbugEntity entity2 = new CheaterbugEntity(1400, 1300, 1.0);
        List<CheaterbugEntity> requestPayload = List.of(entity1, entity2);

        // Call the service method and check results
        CheaterbugResponse response = cheaterbugService.analyze(requestPayload);
        assertNotNull(response);
        assertFalse(cheaterbugService.isSuspicious(response), "Expected no suspicious activity in normal play.");
    }

    @Test
    public void testAnalyze_ExpectedBelow5th_CheatAbove99th_Suspicion() {
        // This case should trigger suspicion due to expected score being low and cheat probability high
        CheaterbugEntity entity1 = new CheaterbugEntity(1200, 2000, 0.1);
        CheaterbugEntity entity2 = new CheaterbugEntity(1000, 1900, 0.2);
        List<CheaterbugEntity> requestPayload = List.of(entity1, entity2);

        // Call the service method and check results
        CheaterbugResponse response = cheaterbugService.analyze(requestPayload);
        assertNotNull(response);
        assertTrue(cheaterbugService.isSuspicious(response), "Expected suspicious activity when expected score is very low and cheat probability is high.");
    }

    @Test
    public void testAnalyze_HighExpectedScore_NoSuspicion() {
        // High expected score (player likely to win); should not be flagged as suspicious
        CheaterbugEntity entity1 = new CheaterbugEntity(1800, 1200, 1.0);
        CheaterbugEntity entity2 = new CheaterbugEntity(1900, 1300, 0.9);
        List<CheaterbugEntity> requestPayload = List.of(entity1, entity2);

        // Call the service method and check results
        CheaterbugResponse response = cheaterbugService.analyze(requestPayload);
        assertNotNull(response);
        assertFalse(cheaterbugService.isSuspicious(response), "Expected no suspicious activity with high expected scores.");
    }

    @Test
    public void testAnalyze_CheatingPatternWithMixedScores() {
        // Mixed scores with one expected to trigger suspicion
        CheaterbugEntity entity1 = new CheaterbugEntity(1500, 1900, 0.5);
        CheaterbugEntity entity2 = new CheaterbugEntity(1400, 1900, 0.9); // High actual, low expected -> suspicious
        List<CheaterbugEntity> requestPayload = List.of(entity1, entity2);

        // Call the service method and check results
        CheaterbugResponse response = cheaterbugService.analyze(requestPayload);
        assertNotNull(response);
        assertTrue(cheaterbugService.isSuspicious(response), "Expected suspicious activity with a high actual score in a low-expected match.");
    }

    @Test
    public void testAnalyze_LowActualScores_NoSuspicion() {
        // All scores are low, no strong indication of cheating
        CheaterbugEntity entity1 = new CheaterbugEntity(1500, 1600, 0.2);
        CheaterbugEntity entity2 = new CheaterbugEntity(1400, 1700, 0.3);
        List<CheaterbugEntity> requestPayload = List.of(entity1, entity2);

        // Call the service method and check results
        CheaterbugResponse response = cheaterbugService.analyze(requestPayload);
        assertNotNull(response);
        assertFalse(cheaterbugService.isSuspicious(response), "Expected no suspicion with uniformly low actual scores.");
    }
}

