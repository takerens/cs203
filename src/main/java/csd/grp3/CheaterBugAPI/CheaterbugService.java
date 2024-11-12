package csd.grp3.CheaterBugAPI;

import csd.grp3.CheaterBugAPI.CheaterbugEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CheaterbugService {

    private final RestTemplate restTemplate;
    private final String cheaterbugUrl;

    public CheaterbugService(RestTemplate restTemplate, 
                             @Value("${cheaterbug.url:http://cheaterbug:8081/cheaterbug/analysis}") String cheaterbugUrl) {
        this.restTemplate = restTemplate;
        this.cheaterbugUrl = cheaterbugUrl;
    }

    public CheaterbugEntity analyze(CheaterbugEntity request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CheaterbugEntity> entity = new HttpEntity<>(request, headers);

        ResponseEntity<CheaterbugEntity> response = restTemplate.exchange(
                cheaterbugUrl,
                HttpMethod.POST,
                entity,
                CheaterbugEntity.class
        );

        CheaterbugEntity result = response.getBody();

        // Check if the response indicates suspicious behavior
        if (result != null && isSuspicious(result)) {
            throw new CheatingSuspicionException("Suspicious activity detected for the request.");
        }

        return result;
    }

    private boolean isSuspicious(CheaterbugEntity result) {
        String cheatProbabilityStr = result.getCheatProbability().get("99thPercentile");
        String expectedWinProbabilityStr = result.getExpectedProbability().get("5thPercentile");

        if (cheatProbabilityStr != null && expectedWinProbabilityStr != null) {
            double cheatProbability = Double.parseDouble(cheatProbabilityStr);
            double expectedWinProbability = Double.parseDouble(expectedWinProbabilityStr);

            return cheatProbability > 0.99 && expectedWinProbability < 0.05;
        }
        return false;
    }
}
