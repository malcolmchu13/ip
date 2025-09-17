package rat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Encapsulates the task list and operations on it.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list seeded with the provided tasks.
     *
     * @param tasks initial tasks (may be null for empty)
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
    }

    /** @return number of tasks in the list */
    public int size() {
        return tasks.size();
    }

    /** @return true if no tasks exist */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Gets the task at a 0-based index.
     *
     * @param index position in the list
     * @return task instance
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "TaskList.get index out of bounds";
        return tasks.get(index);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        assert task != null : "TaskList.add expects a non-null task";
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index 0-based index
     * @return removed task
     */
    public Task remove(int index) {
        assert index >= 0 && index < tasks.size() : "TaskList.remove index out of bounds";
        return tasks.remove(index);
    }

    /**
     * Provides direct access to the underlying list for serialization.
     *
     * @return backing list reference
     */
    public ArrayList<Task> asList() {
        return tasks;
    }

    /**
     * Renders the list to a user-friendly multi-line string.
     *
     * @return formatted list body
     */
    public String toDisplayString() {
        StringBuilder listMessage = new StringBuilder();
        if (tasks.isEmpty()) {
            listMessage.append(" No tasks added yet!\n");
        } else {
            listMessage.append(" Here are the tasks in your list:\n");
            for (int i = 0; i < tasks.size(); i++) {
                listMessage.append(" ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
            }
        }
        return listMessage.toString();
    }

    /**
     * Finds all tasks whose description contains the given keyword (case-insensitive).
     */
    public ArrayList<Task> findTasksByKeyword(String keyword) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        if (keyword == null || keyword.isEmpty()) {
            return foundTasks;
        }
        String needle = keyword.toLowerCase();
        for (Task task : tasks) {
            String desc = task.getDescription();
            if (desc != null && desc.toLowerCase().contains(needle)) {
                foundTasks.add(task);
            }
        }
        return foundTasks;
    }

    /**
     * Finds all tasks (deadlines and events) that occur on the specified date.
     */
    public ArrayList<Task> findTasksByDate(LocalDate searchDate) {
        assert searchDate != null : "TaskList.findTasksByDate expects a search date";
        ArrayList<Task> foundTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                try {
                    java.lang.reflect.Field byField = Deadline.class.getDeclaredField("by");
                    byField.setAccessible(true);
                    LocalDateTime deadlineDateTime = (LocalDateTime) byField.get(deadline);
                    assert deadlineDateTime != null : "Deadline should have a due datetime";
                    if (deadlineDateTime.toLocalDate().equals(searchDate)) {
                        foundTasks.add(task);
                    }
                } catch (Exception ignored) { }
            } else if (task instanceof Event) {
                Event event = (Event) task;
                try {
                    java.lang.reflect.Field fromField = Event.class.getDeclaredField("from");
                    java.lang.reflect.Field toField = Event.class.getDeclaredField("to");
                    fromField.setAccessible(true);
                    toField.setAccessible(true);
                    LocalDateTime fromDateTime = (LocalDateTime) fromField.get(event);
                    LocalDateTime toDateTime = (LocalDateTime) toField.get(event);
                    assert fromDateTime != null : "Event should have a start datetime";
                    assert toDateTime != null : "Event should have an end datetime";
                    LocalDate fromDate = fromDateTime.toLocalDate();
                    LocalDate toDate = toDateTime.toLocalDate();
                    if ((searchDate.isEqual(fromDate) || searchDate.isEqual(toDate) ||
                            (searchDate.isAfter(fromDate) && searchDate.isBefore(toDate)))) {
                        foundTasks.add(task);
                    }
                } catch (Exception ignored) { }
            }
        }
        return foundTasks;
    }
}
