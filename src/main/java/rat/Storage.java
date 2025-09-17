package rat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles persistence of tasks to and from a plain text file.
 */
public class Storage {

    private final String filePath;

    /**
     * Creates a new storage helper bound to the given file path.
     *
     * @param filePath path to the tasks data file
     */
    public Storage(String filePath) {
        assert filePath != null : "Storage requires a non-null file path";
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the backing file, creating the file if it does not exist.
     *
     * @return list of tasks (possibly empty)
     * @throws RatException if the file cannot be read or contains malformed entries
     */
    public ArrayList<Task> load() throws RatException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }

            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    tasks.add(Task.fromString(line));
                }
            }
        } catch (IOException e) {
            throw new RatException("Error loading tasks from file: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Saves the provided tasks to the backing file, overwriting existing content.
     *
     * @param tasks tasks to persist
     * @throws IOException if writing fails
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        assert tasks != null : "Storage.save expects a non-null list";
        FileWriter writer = new FileWriter(filePath);
        for (Task task : tasks) {
            assert task != null : "Storage.save does not persist null tasks";
            writer.write(task.toFileString() + "\n");
        }
        writer.close();
    }
}
