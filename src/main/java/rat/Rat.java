package rat;

import java.io.IOException;


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
            try {
                String fullCommand = ui.readCommand();
                rat.command.Command c = Parser.parse(fullCommand);
                String response = c.execute(tasks, ui, storage);
                ui.printList(response);
                isExit = c.isExit();
            } catch (RatException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Invalid number format. Please enter a valid task number.");
            } finally {
                ui.showLine();
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
/**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            rat.command.Command c = Parser.parse(input.trim());
            return c.execute(tasks, ui, storage);
        } catch (RatException e) {
            return " Oops! " + e.getMessage();
        } catch (NumberFormatException e) {
            return " Oops! Invalid number format. Please enter a valid task number.";
        }
    }
    
}
