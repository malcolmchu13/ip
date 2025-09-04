import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * The main class for the Rat task management application.
 * Provides a command-line interface for managing tasks.
 */
public class Rat {
    private static final String FILE_PATH = "./data/Rat.txt";
    private static Storage storage;
    private static ArrayList<Task> tasks;

    public static void main(String[] args) {
        storage = new Storage(FILE_PATH);
        try {
            tasks = storage.load();
        } catch (RatException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            tasks = new ArrayList<>();
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println("____________________________________________________________\n" +
                          " Hello! I'm rat\n" +
                          " What can I do for you?\n" +
                          "____________________________________________________________");

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                if (input.equalsIgnoreCase("bye")) {
                    break;

                } else if (input.equalsIgnoreCase("list")) {
                    StringBuilder listMessage = new StringBuilder();
                    listMessage.append("____________________________________________________________\n");
                    if (tasks.isEmpty()) {
                        listMessage.append(" No tasks added yet!\n");
                    } else {
                        listMessage.append(" Here are the tasks in your list:\n");
                        for (int i = 0; i < tasks.size(); i++) {
                            listMessage.append(" ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
                        }
                    }
                    listMessage.append("____________________________________________________________");
                    System.out.println(listMessage.toString());

                } else if (input.startsWith("mark")) {
                    String[] parts = input.split(" ");
                    if (parts.length < 2) {
                        throw new RatException("Please provide a task number to mark.");
                    }
                    int index = Integer.parseInt(parts[1]) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        throw new RatException("That task number does not exist.");
                    }
                    tasks.get(index).markAsDone();
                    saveTasks();
                    System.out.println("____________________________________________________________\n" +
                                     " Nice! I've marked this task as done:\n" +
                                     "   " + tasks.get(index).toString() + "\n" +
                                     "____________________________________________________________");

                } else if (input.startsWith("unmark")) {
                    String[] parts = input.split(" ");
                    if (parts.length < 2) {
                        throw new RatException("Please provide a task number to unmark.");
                    }
                    int index = Integer.parseInt(parts[1]) - 1;
                    if (index < 0 || index >= tasks.size()) {
                        throw new RatException("That task number does not exist.");
                    }
                    tasks.get(index).markAsNotDone();
                    saveTasks();
                    System.out.println("____________________________________________________________\n" +
                                     " OK, I've marked this task as not done yet:\n" +
                                     "   " + tasks.get(index).toString() + "\n" +
                                     "____________________________________________________________");

                } else if (input.startsWith("todo")) {
                    String description = input.substring(4).trim();
                    if (description.isEmpty()) {
                        throw new RatException("The description of a todo cannot be empty.");
                    }
                    Task t = new ToDo(description);
                    tasks.add(t);
                    saveTasks();
                    printAdded(t, tasks.size());

                } else if (input.startsWith("deadline")) {
                    String[] parts = input.substring(8).split("/by", 2);
                    String description = parts[0].trim();
                    if (description.isEmpty()) {
                        throw new RatException("The description of a deadline cannot be empty.");
                    }
                    String by = parts.length > 1 ? parts[1].trim() : "unspecified";
                   Task t = new Deadline(description, by);
                   tasks.add(t);
                   saveTasks();
                   printAdded(t, tasks.size());

                } else if (input.startsWith("event")) {
                    String[] parts = input.substring(5).split("/from|/to");
                    String description = parts[0].trim();
                    if (description.isEmpty()) {
                        throw new RatException("The description of an event cannot be empty.");
                    }
                    String from = (parts.length > 1) ? parts[1].trim() : "unspecified";
                    String to = (parts.length > 2) ? parts[2].trim() : "unspecified";
                    Task t = new Event(description, from, to);
                    tasks.add(t);
                    saveTasks();
                    printAdded(t, tasks.size());
                } else if (input.startsWith("delete")) {
                    String[] parts = input.split(" ");
                    if (parts.length < 2) throw new RatException("Please provide a task number to delete.");
                    int index = Integer.parseInt(parts[1]) - 1;
                    if (index < 0 || index >= tasks.size()) throw new RatException("That task number does not exist.");
                    Task removed = tasks.remove(index);
                    saveTasks();
                    System.out.println("____________________________________________________________\n" +
                                     " Noted. I've removed this task:\n" +
                                     "   " + removed + "\n" +
                                     " Now you have " + tasks.size() + " tasks in the list.\n" +
                                     "____________________________________________________________");
                }

                else {
                    throw new RatException("I don't understand that command.");
                }

            } catch (RatException e) {
                System.out.println("____________________________________________________________\n" +
                                 " Oops! " + e.getMessage() + "\n" +
                                 "____________________________________________________________");
            } catch (NumberFormatException e) {
                System.out.println("____________________________________________________________\n" +
                                 " Invalid number format. Please enter a valid task number.\n" +
                                 "____________________________________________________________");
            }
        }

        System.out.println("____________________________________________________________\n" +
                          " Bye. Hope to see you again soon!\n" +
                          "____________________________________________________________");

        scanner.close();
    }

    /**
     * Prints a message confirming that a task has been added.
     * @param task the task that was added
     * @param count the current number of tasks
     */
    private static void printAdded(Task task, int count) {
        System.out.println("____________________________________________________________\n" +
                          " Got it. I've added this task:\n" +
                          "   " + task + "\n" +
                          " Now you have " + count + " tasks in the list.\n" +
                          "____________________________________________________________");
    }

    private static void saveTasks() {
        try {
            storage.save(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}
