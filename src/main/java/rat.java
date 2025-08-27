import java.util.Scanner;

public class rat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm rat");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");

        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("bye")) {
                break;

            } else if (input.equalsIgnoreCase("list")) {
                System.out.println("____________________________________________________________");
                if (taskCount == 0) {
                    System.out.println(" No tasks added yet!");
                } else {
                    System.out.println(" Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                }
                System.out.println("____________________________________________________________");

            } else if (input.startsWith("mark")) {
                int index = Integer.parseInt(input.split(" ")[1]) - 1;
                tasks[index].markAsDone();
                System.out.println("____________________________________________________________");
                System.out.println(" Nice! I've marked this task as done:");
                System.out.println("   " + tasks[index]);
                System.out.println("____________________________________________________________");

            } else if (input.startsWith("unmark")) {
                int index = Integer.parseInt(input.split(" ")[1]) - 1;
                tasks[index].markAsNotDone();
                System.out.println("____________________________________________________________");
                System.out.println(" OK, I've marked this task as not done yet:");
                System.out.println("   " + tasks[index]);
                System.out.println("____________________________________________________________");

            } else if (input.startsWith("todo")) {
                String description = input.substring(5);
                tasks[taskCount] = new ToDo(description);
                taskCount++;
                printAdded(tasks[taskCount - 1], taskCount);

            } else if (input.startsWith("deadline")) {
                String[] parts = input.substring(9).split("/by", 2);
                String description = parts[0].trim();
                String by = parts.length > 1 ? parts[1].trim() : "unspecified";
                tasks[taskCount] = new Deadline(description, by);
                taskCount++;
                printAdded(tasks[taskCount - 1], taskCount);

            } else if (input.startsWith("event")) {
                String[] parts = input.substring(6).split("/from|/to");
                String description = parts[0].trim();
                String from = (parts.length > 1) ? parts[1].trim() : "unspecified";
                String to = (parts.length > 2) ? parts[2].trim() : "unspecified";
                tasks[taskCount] = new Event(description, from, to);
                taskCount++;
                printAdded(tasks[taskCount - 1], taskCount);

            } else {
                System.out.println(" I don't understand that command.");
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
