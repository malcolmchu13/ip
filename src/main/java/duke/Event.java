package duke;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a description, start time, and end time.
 */
public class Event extends Task{
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates an event with concrete start and end timestamps.
     *
     * @param description task description
     * @param from start date/time
     * @param to end date/time
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Creates an event parsing the provided start/end text values.
     *
     * @param description task description
     * @param from start date/time text in supported formats
     * @param to end date/time text in supported formats
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
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
     * Returns a display string including the time range.
     *
     * @return formatted string for lists
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[E]").append(super.toString()).append(" (from: ").append(formatDateTime(from)).append(" to: ").append(formatDateTime(to)).append(")");
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
     * Encodes this event for storage.
     *
     * @return storage line: "E | doneFlag | description | ISO_FROM | ISO_TO"
     */
    public String toFileString() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from.toString() + " | " + to.toString();
    }
}
