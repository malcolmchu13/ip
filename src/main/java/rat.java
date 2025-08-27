import java.util.Scanner;

public class rat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];  // store Task objects
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
                try {
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsDone();
                        System.out.println("____________________________________________________________");
                        System.out.println(" Nice! I've marked this task as done:");
                        System.out.println("   " + tasks[index]);
                        System.out.println("____________________________________________________________");
                    } else {
                        System.out.println(" Invalid task number.");
                    }
                } catch (Exception e) {
                    System.out.println(" Usage: mark <task_number>");
                }

            } else if (input.startsWith("unmark")) {
                try {
                    int index = Integer.parseInt(input.split(" ")[1]) - 1;
                    if (index >= 0 && index < taskCount) {
                        tasks[index].markAsNotDone();
                        System.out.println("____________________________________________________________");
                        System.out.println(" OK, I've marked this task as not done yet:");
                        System.out.println("   " + tasks[index]);
                        System.out.println("____________________________________________________________");
                    } else {
                        System.out.println(" Invalid task number.");
                    }
                } catch (Exception e) {
                    System.out.println(" Usage: unmark <task_number>");
                }

            } else {
                // Add task
                if (taskCount < tasks.length) {
                    tasks[taskCount] = new Task(input);
                    taskCount++;
                    System.out.println("____________________________________________________________");
                    System.out.println(" added: " + input);
                    System.out.println("____________________________________________________________");
                } else {
                    System.out.println("____________________________________________________________");
                    System.out.println(" Sorry, task list is full (max 100).");
                    System.out.println("____________________________________________________________");
                }
            }
        }

        System.out.println("____________________________________________________________");
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }
}
