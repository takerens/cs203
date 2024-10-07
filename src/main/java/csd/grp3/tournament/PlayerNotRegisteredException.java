package csd.grp3.tournament;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerNotRegisteredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PlayerNotRegisteredException(String message) {
        super(message);
    }
}