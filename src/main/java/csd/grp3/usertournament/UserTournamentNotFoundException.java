package csd.grp3.usertournament;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserTournamentNotFoundException extends RuntimeException{
    public UserTournamentNotFoundException() {
        super("Tournament entry could not be found");
    }
}
