package rat.command;

import rat.Deadline;
import rat.Event;
import rat.RatException;
import rat.Storage;
import rat.Task;
import rat.TaskList;
import rat.Ui;

/**
 * Command that reschedules deadline or event tasks to a new time.
 */
public class SnoozeCommand extends Command {
    private final int index;
    private final String newBy; // nullable, used for deadlines
    private final String newFrom; // nullable, used for events
    private final String newTo; // nullable, used for events

    public SnoozeCommand(int index, String newBy, String newFrom, String newTo) {
        this.index = index;
        this.newBy = newBy;
        this.newFrom = newFrom;
        this.newTo = newTo;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        assert tasks != null : "SnoozeCommand expects a TaskList";
        assert ui != null : "SnoozeCommand expects a Ui";
        assert storage != null : "SnoozeCommand expects storage";
        if (index < 0 || index >= tasks.size()) {
            throw new RatException("That task number does not exist.");
        }

        Task task = tasks.get(index);
        if (task instanceof Deadline) {
            if (newBy == null || newBy.isBlank()) {
                throw new RatException("Please specify the new deadline using /by.");
            }
            Deadline deadline = (Deadline) task;
            deadline.reschedule(newBy);
        } else if (task instanceof Event) {
            if (newFrom == null || newFrom.isBlank() || newTo == null || newTo.isBlank()) {
                throw new RatException("Please specify both /from and /to for events when snoozing.");
            }
            Event event = (Event) task;
            event.reschedule(newFrom, newTo);
        } else {
            throw new RatException("Only deadlines and events can be snoozed.");
        }

        try {
            storage.save(tasks.asList());
        } catch (java.io.IOException e) {
            // Persisting failure should not crash the app; surface via UI instead.
        }

        return " Got it. I've rescheduled this task:\n   " + task;
    }
}
