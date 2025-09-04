/**
 * Represents an event task with a description, start time, and end time.
 */
public class Event extends Task{
    /**
     * The start time of the event.
     */
    protected String from;
    /**
     * The end time of the event.
     */
    protected String to;

    /**
     * Creates an event task with the given description, start time, and end time.
     * @param description the task description
     * @param from the start time
     * @param to the end time
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[E]").append(super.toString()).append(" (from: ").append(from).append(" to: ").append(to).append(")");
        return sb.toString();
    }

    @Override
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }
}
