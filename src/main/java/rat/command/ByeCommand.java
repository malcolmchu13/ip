package rat.command;

import rat.RatException;
import rat.Storage;
import rat.TaskList;
import rat.Ui;

public class ByeCommand extends Command {
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        assert tasks != null : "ByeCommand expects a TaskList";
        assert ui != null : "ByeCommand expects a Ui";
        assert storage != null : "ByeCommand expects storage";
        return "Bye. Hope to see you again soon!";
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
