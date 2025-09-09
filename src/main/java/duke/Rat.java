package duke;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The main class for the Rat task management application.
 * Provides a command-line interface for managing tasks, using Ui, Storage, Parser, and TaskList.
 */
public class Rat {
    private static final String FILE_PATH = "./data/Rat.txt";

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public Rat(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (RatException e) {
            ui.showLoadingError();
            loaded = new TaskList();
        }
        this.tasks = loaded;
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String input = ui.readCommand();
            try {
                Parser.ParsedCommand cmd = Parser.parse(input);
                switch (cmd.type) {
                    case BYE:
                        isExit = true;
                        break;
                    case LIST:
                        ui.printList(tasks.toDisplayString());
                        break;
                    case MARK: {
                        int index = cmd.index;
                        if (index < 0 || index >= tasks.size()) throw new RatException("That task number does not exist.");
                        tasks.get(index).markAsDone();
                        saveTasks();
                        ui.printMarked(tasks.get(index));
                        break;
                    }
                    case UNMARK: {
                        int index = cmd.index;
                        if (index < 0 || index >= tasks.size()) throw new RatException("That task number does not exist.");
                        tasks.get(index).markAsNotDone();
                        saveTasks();
                        ui.printUnmarked(tasks.get(index));
                        break;
                    }
                    case TODO: {
                        Task t = new ToDo(cmd.description);
                        tasks.add(t);
                        saveTasks();
                        ui.printAdded(t, tasks.size());
                        break;
                    }
                    case DEADLINE: {
                        Task t = new Deadline(cmd.description, cmd.by);
                        tasks.add(t);
                        saveTasks();
                        ui.printAdded(t, tasks.size());
                        break;
                    }
                    case EVENT: {
                        Task t = new Event(cmd.description, cmd.from, cmd.to);
                        tasks.add(t);
                        saveTasks();
                        ui.printAdded(t, tasks.size());
                        break;
                    }
                    case DELETE: {
                        int index = cmd.index;
                        if (index < 0 || index >= tasks.size()) throw new RatException("That task number does not exist.");
                        Task removed = tasks.remove(index);
                        saveTasks();
                        ui.printDeleted(removed, tasks.size());
                        break;
                    }
                    case FIND: {
                        LocalDate searchDate = cmd.date;
                        java.util.ArrayList<Task> found = tasks.findTasksByDate(searchDate);
                        StringBuilder sb = new StringBuilder();
                        if (found.isEmpty()) {
                            sb.append(" No tasks found on ")
                              .append(searchDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")))
                              .append("!\n");
                        } else {
                            sb.append(" Here are the tasks on ")
                              .append(searchDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")))
                              .append(":\n");
                            for (int i = 0; i < found.size(); i++) {
                                sb.append(" ").append(i + 1).append(". ").append(found.get(i)).append("\n");
                            }
                        }
                        ui.printList(sb.toString());
                        break;
                    }
                    default:
                        throw new RatException("I don't understand that command.");
                }
            } catch (RatException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Invalid number format. Please enter a valid task number.");
            }
        }
        ui.printBye();
    }

    private void saveTasks() {
        try {
            storage.save(tasks.asList());
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Rat(FILE_PATH).run();
    }
}
