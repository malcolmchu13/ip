package rat.command;

import rat.RatException;
import rat.Storage;
import rat.Task;
import rat.TaskList;
import rat.Ui;

public class UnmarkCommand extends Command {
    private final int index; // 0-based

    public UnmarkCommand(int index) { this.index = index; }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        assert tasks != null : "UnmarkCommand expects a TaskList";
        assert ui != null : "UnmarkCommand expects a Ui";
        assert storage != null : "UnmarkCommand expects storage";
        if (index < 0 || index >= tasks.size()) {
            throw new RatException("That task number does not exist.");
        }
        Task t = tasks.get(index);
        t.markAsNotDone();
        try {
            storage.save(tasks.asList());
        } catch (java.io.IOException e) {
            // ignore save error for UI response
        }
        return " OK, I've marked this task as not done yet:\n   " + t;
    }
}
