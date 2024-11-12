package csd.grp3.CheaterBugAPI;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cheaterbug")
public class CheaterbugController {

    private final CheaterbugService cheaterbugService;

    public CheaterbugController(CheaterbugService cheaterbugService) {
        this.cheaterbugService = cheaterbugService;
    }

    /**
     * Endpoint to analyze data using the Cheaterbug API.
     *
     * @param request The data to be analyzed, wrapped in CheaterbugEntity.
     * @return The response from the Cheaterbug API wrapped in a ResponseEntity.
     */
    @PostMapping("/analysis")
    public ResponseEntity<CheaterbugEntity> analyze(
            @Valid @RequestBody CheaterbugEntity request) {
        CheaterbugEntity response = cheaterbugService.analyze(request);
        return ResponseEntity.ok(response);
    }
}
