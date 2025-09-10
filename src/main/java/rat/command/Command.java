package rat.command;

import rat.RatException;
import rat.Storage;
import rat.TaskList;
import rat.Ui;

/**
 * Abstract command for the Rat application.
 */
public abstract class Command {
    /**
     * Executes this command and returns a user-facing message.
     */
    public abstract String execute(TaskList tasks, Ui ui, Storage storage) throws RatException;

    /**
     * @return true if this command should exit the application.
     */
    public boolean isExit() { return false; }
}

