package rat.command;

import rat.Deadline;
import rat.RatException;
import rat.Storage;
import rat.Task;
import rat.TaskList;
import rat.Ui;

public class DeadlineCommand extends Command {
    private final String description;
    private final String by;

    public DeadlineCommand(String description, String by) {
        this.description = description;
        this.by = by;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        if (description == null || description.isBlank()) {
            throw new RatException("The description of a deadline cannot be empty.");
        }
        Task t = new Deadline(description, by);
        tasks.add(t);
        try {
            storage.save(tasks.asList());
        } catch (java.io.IOException e) {
            // ignore save error for UI response
        }
        return " Got it. I've added this task:\n   " + t + "\n Now you have " + tasks.size() + " tasks in the list.";
    }
}

