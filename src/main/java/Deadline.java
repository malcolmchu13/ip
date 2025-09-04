/**
 * Represents a deadline task with a description and a due date.
 */
public class Deadline extends Task {

    /**
     * The due date of the deadline.
     */
    protected String by;

    /**
     * Creates a deadline task with the given description and due date.
     * @param description the task description
     * @param by the due date
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[D]").append(super.toString()).append(" (by: ").append(by).append(")");
        return sb.toString();
    }

    @Override
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }
}
