package csd.grp3.match;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MatchNotFoundException extends RuntimeException{

    public MatchNotFoundException() {
        super("match not found");
    }

    public MatchNotFoundException(String message) {
        super(message);
    }
}