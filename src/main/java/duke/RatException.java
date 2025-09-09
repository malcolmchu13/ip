package duke;

/**
 * Custom exception for the Rat application.
 * Thrown when there are errors in task management operations.
 */
public class RatException extends Exception{
    public RatException(String message) {
        super(message);
    }
}
