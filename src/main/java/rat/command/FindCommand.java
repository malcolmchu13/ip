package rat.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import rat.RatException;
import rat.Storage;
import rat.Task;
import rat.TaskList;
import rat.Ui;

public class FindCommand extends Command {
    private final String keyword; // nullable
    private final LocalDate date; // nullable

    public static FindCommand byKeyword(String keyword) {
        return new FindCommand(keyword, null);
    }

    public static FindCommand byDate(LocalDate date) {
        return new FindCommand(null, date);
    }

    private FindCommand(String keyword, LocalDate date) {
        this.keyword = keyword;
        this.date = date;
    }

    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) throws RatException {
        if (date != null) {
            ArrayList<Task> found = tasks.findTasksByDate(date);
            StringBuilder sb = new StringBuilder();
            if (found.isEmpty()) {
                sb.append(" No tasks found on ")
                  .append(date.format(DateTimeFormatter.ofPattern("MMM dd yyyy")))
                  .append("!");
            } else {
                sb.append(" Here are the tasks on ")
                  .append(date.format(DateTimeFormatter.ofPattern("MMM dd yyyy")))
                  .append(":\n");
                for (int i = 0; i < found.size(); i++) {
                    sb.append(" ").append(i + 1).append(". ").append(found.get(i)).append("\n");
                }
            }
            return sb.toString();
        } else if (keyword != null && !keyword.isBlank()) {
            ArrayList<Task> found = tasks.findTasksByKeyword(keyword);
            StringBuilder sb = new StringBuilder();
            if (found.isEmpty()) {
                sb.append(" No matching tasks found.");
            } else {
                sb.append(" Here are the matching tasks in your list:\n");
                for (int i = 0; i < found.size(); i++) {
                    sb.append(" ").append(i + 1).append(". ").append(found.get(i)).append("\n");
                }
            }
            return sb.toString();
        }
        throw new RatException("Please provide a keyword or a date to search.");
    }
}

