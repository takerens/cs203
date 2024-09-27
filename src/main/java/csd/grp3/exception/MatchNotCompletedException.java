package csd.grp3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MatchNotCompletedException extends RuntimeException {
    
    public MatchNotCompletedException(Long id) {
        super("Match " + id + " not completed yet.");
    }
}
