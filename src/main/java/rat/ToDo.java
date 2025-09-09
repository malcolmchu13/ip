package rat;

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
    /**
     * Returns a display string prefixed with the todo tag.
     *
     * @return formatted string for lists
     */
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    /**
     * Encodes this todo for storage.
     *
     * @return storage line: "T | doneFlag | description"
     */
    public String toFileString() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }
}
