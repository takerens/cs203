package csd.grp3.tournament;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerAlreadyRegisteredException extends RuntimeException {
    
    public PlayerAlreadyRegisteredException() {
        super("Player has already registered for this tournament.");
    }
}
