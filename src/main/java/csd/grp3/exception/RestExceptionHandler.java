package csd.grp3.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import csd.grp3.tournament.TournamentNotFoundException;
import csd.grp3.tournament.PlayerNotRegisteredException;

/**
 * Centralize exception handling in this class.
 */
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlayerNotRegisteredException.class)
    public ResponseEntity<String> handlePlayerNotRegisteredException(PlayerNotRegisteredException ex) {
        String message = "Error: " + ex.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

     @ExceptionHandler(TournamentNotFoundException.class)
    public ResponseEntity<String> handleTournamentNotFoundException(TournamentNotFoundException ex) {
        String message = "Error: Tournament not found with ID: " + ex.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
