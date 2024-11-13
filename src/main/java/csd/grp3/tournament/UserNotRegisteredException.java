package csd.grp3.tournament;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotRegisteredException extends RuntimeException {

    public UserNotRegisteredException() {
        super("User did not register for this tournament.");
    }
}