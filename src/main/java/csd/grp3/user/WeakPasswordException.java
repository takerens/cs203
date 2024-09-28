package csd.grp3.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WeakPasswordException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public WeakPasswordException(String message) {
        super(message);
    }
}
