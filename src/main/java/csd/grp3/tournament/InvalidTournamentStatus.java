package csd.grp3.tournament;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidTournamentStatus extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public InvalidTournamentStatus(String msg) {
        super(msg);
    }
}
