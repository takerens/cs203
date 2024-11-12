package csd.grp3.CheaterBugAPI;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@WebMvcTest(CheaterBugController.class)
public class CheaterBugControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CheaterbugService cheaterbugService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAnalyzeEndpoint() throws Exception {
        // Prepare mock data for request and response
        CheaterbugEntity entity1 = new CheaterbugEntity(0.8, 0.5);
        CheaterbugEntity entity2 = new CheaterbugEntity(0.7, 0.6);
        List<CheaterbugEntity> request = List.of(entity1, entity2);

        Map<String, String> cheatProbability = Map.of("99thPercentile", "0.95");
        Map<String, String> expectedProbability = Map.of("5thPercentile", "0.04");
        CheaterbugResponse mockResponse = new CheaterbugResponse(cheatProbability, expectedProbability);

        // Mock the service response
        Mockito.when(cheaterbugService.analyze(request)).thenReturn(mockResponse);

        // Perform the POST request
        mockMvc.perform(post("/cheaterbug/analysis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cheatProbability.99thPercentile", is("0.95")))
                .andExpect(jsonPath("$.expectedProbability.5thPercentile", is("0.04")));
    }
}
