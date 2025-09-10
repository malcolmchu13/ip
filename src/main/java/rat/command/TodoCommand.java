package rat.command;

import rat.RatException;
import rat.Storage;
import rat.TaskList;
import rat.ToDo;
import rat.Ui;
import rat.Task;

public class TodoCommand extends Command {
    private final String description;

    public TodoCommand(String description) { this.description = description; }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        if (description == null || description.isBlank()) {
            throw new RatException("The description of a todo cannot be empty.");
        }
        Task t = new ToDo(description);
        tasks.add(t);
        try {
            storage.save(tasks.asList());
        } catch (java.io.IOException e) {
            // ignore save error for UI response
        }
        return " Got it. I've added this task:\n   " + t + "\n Now you have " + tasks.size() + " tasks in the list.";
    }
}

