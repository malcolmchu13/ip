package rat.command;

import rat.RatException;
import rat.Storage;
import rat.TaskList;
import rat.Ui;

public class ListCommand extends Command {
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        assert tasks != null : "ListCommand expects a TaskList";
        assert ui != null : "ListCommand expects a Ui";
        assert storage != null : "ListCommand expects storage";
        return tasks.toDisplayString();
    }
}
