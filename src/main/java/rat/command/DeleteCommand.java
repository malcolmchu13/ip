package rat.command;

import rat.RatException;
import rat.Storage;
import rat.Task;
import rat.TaskList;
import rat.Ui;

public class DeleteCommand extends Command {
    private final int index; // 0-based

    public DeleteCommand(int index) { this.index = index; }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        if (index < 0 || index >= tasks.size()) {
            throw new RatException("That task number does not exist.");
        }
        Task removed = tasks.remove(index);
        try {
            storage.save(tasks.asList());
        } catch (java.io.IOException e) {
            // ignore save error for UI response
        }
        return " Noted. I've removed this task:\n   " + removed + "\n Now you have " + tasks.size() + " tasks in the list.";
    }
}

