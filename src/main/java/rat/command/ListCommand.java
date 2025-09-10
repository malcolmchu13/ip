package rat.command;

import rat.RatException;
import rat.Storage;
import rat.TaskList;
import rat.Ui;

public class ListCommand extends Command {
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        return tasks.toDisplayString();
    }
}

