package duke;

import java.util.Scanner;

/**
 * Handles user interactions: reading input and printing messages.
 */
public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    /** Prints the welcome banner. */
    public void showWelcome() {
        System.out.println("____________________________________________________________\n" +
                " Hello! I'm rat\n" +
                " What can I do for you?\n" +
                "____________________________________________________________");
    }

    /**
     * Legacy no-op retained for API compatibility. The line separators are
     * printed as part of message methods in this implementation.
     */
    public void showLine() {
        // In this app, the line is printed as part of each message.
        // Method kept for API completeness.
    }

    /**
     * Reads one trimmed line of user input from stdin.
     *
     * @return trimmed input line
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays an error banner with the provided message.
     *
     * @param message error text
     */
    public void showError(String message) {
        System.out.println("____________________________________________________________\n" +
                " Oops! " + message + "\n" +
                "____________________________________________________________");
    }

    /** Shows a generic loading error message. */
    public void showLoadingError() {
        showError("Error loading tasks from file.");
    }

    /**
     * Prints a body inside the standard banner.
     *
     * @param body content to display
     */
    public void printList(String body) {
        System.out.println("____________________________________________________________\n" +
                body +
                "____________________________________________________________");
    }

    /**
     * Prints a confirmation for adding a task with the new count.
     *
     * @param task the task added
     * @param count current number of tasks
     */
    public void printAdded(Task task, int count) {
        System.out.println("____________________________________________________________\n" +
                " Got it. I've added this task:\n" +
                "   " + task + "\n" +
                " Now you have " + count + " tasks in the list.\n" +
                "____________________________________________________________");
    }

    /** Prints a confirmation that a task was marked done. */
    public void printMarked(Task task) {
        System.out.println("____________________________________________________________\n" +
                " Nice! I've marked this task as done:\n" +
                "   " + task + "\n" +
                "____________________________________________________________");
    }

    /** Prints a confirmation that a task was marked not done. */
    public void printUnmarked(Task task) {
        System.out.println("____________________________________________________________\n" +
                " OK, I've marked this task as not done yet:\n" +
                "   " + task + "\n" +
                "____________________________________________________________");
    }

    /**
     * Prints a confirmation that a task was deleted, including the new count.
     *
     * @param task removed task
     * @param count remaining task count
     */
    public void printDeleted(Task task, int count) {
        System.out.println("____________________________________________________________\n" +
                " Noted. I've removed this task:\n" +
                "   " + task + "\n" +
                " Now you have " + count + " tasks in the list.\n" +
                "____________________________________________________________");
    }

    /** Prints the goodbye banner. */
    public void printBye() {
        System.out.println("____________________________________________________________\n" +
                " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________");
    }
}
