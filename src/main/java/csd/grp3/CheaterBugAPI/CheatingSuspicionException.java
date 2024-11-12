package csd.grp3.CheaterBugAPI;

public class CheatingSuspicionException extends RuntimeException {

    /**
     * Constructor with a custom message for the exception.
     * 
     * @param message A descriptive message about the cheating suspicion.
     */
    public CheatingSuspicionException(String message) {
        super(message);
    }

    /**
     * Constructor with a custom message and cause for the exception.
     * 
     * @param message A descriptive message about the cheating suspicion.
     * @param cause The underlying cause of the exception.
     */
    public CheatingSuspicionException(String message, Throwable cause) {
        super(message, cause);
    }
}
