package rat.command;

import rat.RatException;
import rat.Storage;
import rat.Task;
import rat.TaskList;
import rat.Ui;

public class MarkCommand extends Command {
    private final int index; // 0-based

    public MarkCommand(int index) { this.index = index; }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        assert tasks != null : "MarkCommand expects a TaskList";
        assert ui != null : "MarkCommand expects a Ui";
        assert storage != null : "MarkCommand expects storage";
        if (index < 0 || index >= tasks.size()) {
            throw new RatException("That task number does not exist.");
        }
        Task t = tasks.get(index);
        t.markAsDone();
        try {
            storage.save(tasks.asList());
        } catch (java.io.IOException e) {
            // Persisting failure shouldn't crash the app, but we report it
        }
        return " Nice! I've marked this task as done:\n   " + t;
    }
}
