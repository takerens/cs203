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
        CheaterbugEntity entity1 = new CheaterbugEntity(1200,1800, 0.5);
        CheaterbugEntity entity2 = new CheaterbugEntity(1200,1100, 1.0);
        List<CheaterbugEntity> requestPayload = List.of(entity1, entity2);

        // Call the service method
        CheaterbugResponse response = cheaterbugService.analyze(requestPayload);

        // Assertions to verify the response
        assertNotNull(response, "The response should not be null");
        assertNotNull(response.getCheatProbability(), "Cheat probability should not be null");
        assertNotNull(response.getExpectedProbability(), "Expected probability should not be null");

        // Check specific values if known or expected structure
        assertTrue(response.getCheatProbability().containsKey("99thPercentile"));
        assertTrue(response.getExpectedProbability().containsKey("5thPercentile"));
    }
}
