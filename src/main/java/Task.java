/**
 * Represents a task with a description and completion status.
 * This is an abstract base class for different types of tasks.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }
    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(getStatusIcon()).append("] ").append(description);
        return sb.toString();
    }

    public abstract String toFileString();

    public static Task fromString(String fileString) throws RatException {
        String[] parts = fileString.split(" \\| ");
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
