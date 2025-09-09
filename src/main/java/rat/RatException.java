package rat;

/**
 * Custom exception for the Rat application.
 * Thrown when there are errors in task management operations.
 */
public class RatException extends Exception{
    /**
     * Constructs a Rat-specific exception with the provided message.
     *
     * @param message human-readable error description
     */
    public RatException(String message) {
        super(message);
    }
}
