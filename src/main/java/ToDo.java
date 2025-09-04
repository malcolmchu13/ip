/**
 * Represents a todo task with a description.
 */
public class ToDo extends Task{
    /**
     * Creates a todo task with the given description.
     * @param description the task description
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
