package rat;

import java.util.Scanner;

/**
 * Handles user interactions: reading input and printing messages.
 */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        System.out.println("____________________________________________________________\n" +
                " Hello! I'm rat\n" +
                " What can I do for you?\n" +
                "____________________________________________________________");
    }

    public void showLine() {
        // In this app, the line is printed as part of each message.
        // Method kept for API completeness.
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showError(String message) {
        System.out.println("____________________________________________________________\n" +
                " Oops! " + message + "\n" +
                "____________________________________________________________");
    }

    public void showLoadingError() {
        showError("Error loading tasks from file.");
    }

    public void printList(String body) {
        System.out.println("____________________________________________________________\n" +
                body +
                "____________________________________________________________");
    }

    public void printAdded(Task task, int count) {
        System.out.println("____________________________________________________________\n" +
                " Got it. I've added this task:\n" +
                "   " + task + "\n" +
                " Now you have " + count + " tasks in the list.\n" +
                "____________________________________________________________");
    }

    public void printMarked(Task task) {
        System.out.println("____________________________________________________________\n" +
                " Nice! I've marked this task as done:\n" +
                "   " + task + "\n" +
                "____________________________________________________________");
    }

    public void printUnmarked(Task task) {
        System.out.println("____________________________________________________________\n" +
                " OK, I've marked this task as not done yet:\n" +
                "   " + task + "\n" +
                "____________________________________________________________");
    }

    public void printDeleted(Task task, int count) {
        System.out.println("____________________________________________________________\n" +
                " Noted. I've removed this task:\n" +
                "   " + task + "\n" +
                " Now you have " + count + " tasks in the list.\n" +
                "____________________________________________________________");
    }

    public void printBye() {
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________");
    }
}
