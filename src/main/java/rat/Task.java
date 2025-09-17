package rat;

/**
 * Represents a task with a description and completion status.
 * This is an abstract base class for different types of tasks.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a task with the given description. New tasks start as not done.
     *
     * @param description human-readable task description
     */
    public Task(String description) {
        assert description != null : "Task description should not be null";
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon used in list displays.
     *
     * @return "X" if done, otherwise a single space
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }
    public String getDescription() {
        return this.description;
    }
    /** Marks this task as done. */
    public void markAsDone() {
        this.isDone = true;
    }

    /** Marks this task as not done. */
    public void markAsNotDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getStatusIcon()).append("] ").append(description);
        return sb.toString();
    }

    /**
     * Serializes this task to a single-line storage format.
     *
     * @return encoded string for storage
     */
    public abstract String toFileString();

    /**
     * Deserializes a task from the storage format produced by {@link #toFileString()}.
     *
     * @param fileString encoded task line
     * @return reconstructed task instance
     * @throws RatException if the encoded line is malformed or type is unknown
     */
    public static Task fromString(String fileString) throws RatException {
        assert fileString != null : "Persisted task string should not be null";
        String[] parts = fileString.split(" \\| ");
        assert parts.length >= 3 : "Persisted task string must include type, status, and description";
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        Task task;
        switch (type) {
        case "T":
            task = new ToDo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new RatException("Invalid format for deadline task in file.");
            }
            String by = parts[3];
            task = new Deadline(description, by);
            break;
        case "E":
            if (parts.length < 5) {
                throw new RatException("Invalid format for event task in file.");
            }
            String from = parts[3];
            String to = parts[4];
            task = new Event(description, from, to);
            break;
        default:
            throw new RatException("Unknown task type in file: " + type);
        }
        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
