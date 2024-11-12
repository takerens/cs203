package csd.grp3.CheaterBugAPI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CheaterbugService {

    private final RestTemplate restTemplate;
    private final String cheaterbugUrl;

    public CheaterbugService(RestTemplate restTemplate, 
                             @Value("${cheaterbug.url:http://localhost:8081/cheaterbug/analysis}") String cheaterbugUrl) {
        this.restTemplate = restTemplate;
        this.cheaterbugUrl = cheaterbugUrl;
    }

    public CheaterbugResponse analyze(List<CheaterbugEntity> results) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Wrap the list in an HttpEntity and send it to the API
        HttpEntity<List<CheaterbugEntity>> entity = new HttpEntity<>(results, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                cheaterbugUrl,
                HttpMethod.POST,
                entity,
                Map.class
        );

        // Parse the API response into CheaterbugResponse
        Map<String, String> cheatProbability = (Map<String, String>) response.getBody().get("cheatProbability");
        Map<String, String> expectedProbability = (Map<String, String>) response.getBody().get("expectedProbability");

        return new CheaterbugResponse(cheatProbability, expectedProbability);
    }
}
