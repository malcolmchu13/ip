package duke;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a description and a due date.
 */
public class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Creates a deadline with a concrete timestamp.
     *
     * @param description task description
     * @param by due date/time
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Creates a deadline parsing the provided date/time text.
     *
     * @param description task description
     * @param by date/time in supported formats
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = parseDateTime(by);
    }

    /**
     * Parses a date/time string into LocalDateTime.
     * Supports formats: yyyy-MM-dd, yyyy-MM-dd HHmm, dd/MM/yyyy HHmm, yyyy-MM-ddTHH:mm
     * @param dateTimeStr the date/time string to parse
     * @return LocalDateTime object
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            // Try ISO format first (from file loading)
            if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}")) {
                return LocalDateTime.parse(dateTimeStr);
            }
            // Try yyyy-MM-dd format
            else if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDateTime.parse(dateTimeStr + "T00:00");
            }
            // Try yyyy-MM-dd HHmm format
            else if (dateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{4}")) {
                String[] parts = dateTimeStr.split(" ");
                String time = parts[1];
                String formattedTime = time.substring(0, 2) + ":" + time.substring(2);
                return LocalDateTime.parse(parts[0] + "T" + formattedTime);
            }
            // Try dd/MM/yyyy HHmm format
            else if (dateTimeStr.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{4}")) {
                String[] parts = dateTimeStr.split(" ");
                String[] dateParts = parts[0].split("/");
                String time = parts[1];
                String formattedTime = time.substring(0, 2) + ":" + time.substring(2);
                String isoDate = dateParts[2] + "-" + String.format("%02d", Integer.parseInt(dateParts[1])) + "-" + String.format("%02d", Integer.parseInt(dateParts[0]));
                return LocalDateTime.parse(isoDate + "T" + formattedTime);
            }
            // Default to current time if parsing fails
            return LocalDateTime.now();
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    @Override
    /**
     * Returns a display string including the due date/time.
     *
     * @return formatted string for lists
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[D]").append(super.toString()).append(" (by: ").append(formatDateTime(by)).append(")");
        return sb.toString();
    }

    /**
     * Formats LocalDateTime to readable string format.
     * @param dateTime the LocalDateTime to format
     * @return formatted string in "MMM dd yyyy hh:mm a" format
     */
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
        return dateTime.format(formatter);
    }

    @Override
    /**
     * Encodes this deadline for storage.
     *
     * @return storage line: "D | doneFlag | description | ISO_LOCAL_DATE_TIME"
     */
    public String toFileString() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by.toString();
    }
}
