import java.util.Scanner;
import java.util.ArrayList;
public class Rat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm rat");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        while (true) {
            String input = scanner.nextLine().trim();

            try {
                if (input.equalsIgnoreCase("bye")) {
                    break;

                } else if (input.equalsIgnoreCase("list")) {
                    System.out.println("____________________________________________________________");
                    if (tasks.isEmpty()) {
                        System.out.println(" No tasks added yet!");
                    } else {
                        System.out.println(" Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
                        }
                    }
                    System.out.println("____________________________________________________________");

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
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + tasks.get(index).toString());
                    System.out.println("____________________________________________________________");

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
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + tasks.get(index).toString());
                    System.out.println("____________________________________________________________");

                } else if (input.startsWith("todo")) {
                    String description = input.substring(4).trim();
                    if (description.isEmpty()) {
                        throw new RatException("The description of a todo cannot be empty.");
                    }
                    Task t = new ToDo(description);
                    tasks.add(t);
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
                    printAdded(t, tasks.size());
                } else if (input.startsWith("delete")) {
                    String[] parts = input.split(" ");
                    if (parts.length < 2) throw new RatException("Please provide a task number to delete.");
                    int index = Integer.parseInt(parts[1]) - 1;
                    if (index < 0 || index >= tasks.size()) throw new RatException("That task number does not exist.");
                    Task removed = tasks.remove(index);
                    System.out.println("____________________________________________________________");
                    System.out.println(" Noted. I've removed this task:");
                    System.out.println("   " + removed);
                    System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                }

                else {
                    throw new RatException("I don't understand that command.");
                }

            } catch (RatException e) {
                System.out.println("____________________________________________________________");
                System.out.println(" Oops! " + e.getMessage());
                System.out.println("____________________________________________________________");
            } catch (NumberFormatException e) {
                System.out.println("____________________________________________________________");
                System.out.println(" Invalid number format. Please enter a valid task number.");
                System.out.println("____________________________________________________________");
            }
        }

        System.out.println("____________________________________________________________");
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }

    private static void printAdded(Task task, int count) {
        System.out.println("____________________________________________________________");
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + count + " tasks in the list.");
        System.out.println("____________________________________________________________");
    }
}
