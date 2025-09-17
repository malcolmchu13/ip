package rat.command;

import rat.Event;
import rat.RatException;
import rat.Storage;
import rat.Task;
import rat.TaskList;
import rat.Ui;

public class EventCommand extends Command {
    private final String description;
    private final String from;
    private final String to;

    public EventCommand(String description, String from, String to) {
        this.description = description;
        this.from = from;
        this.to = to;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        assert tasks != null : "EventCommand expects a TaskList";
        assert ui != null : "EventCommand expects a Ui";
        assert storage != null : "EventCommand expects storage";
        if (description == null || description.isBlank()) {
            throw new RatException("The description of an event cannot be empty.");
        }
        Task t = new Event(description, from, to);
        tasks.add(t);
        try {
            storage.save(tasks.asList());
        } catch (java.io.IOException e) {
            // ignore save error for UI response
        }
        return " Got it. I've added this task:\n   " + t + "\n Now you have " + tasks.size() + " tasks in the list.";
    }
}
