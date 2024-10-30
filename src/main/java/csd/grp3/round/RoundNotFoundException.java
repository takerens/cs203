package csd.grp3.round;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoundNotFoundException extends RuntimeException {
    
    public RoundNotFoundException() {
        super("round not found");
    }
}
