package rat;

import java.time.LocalDate;

import rat.command.ByeCommand;
import rat.command.Command;
import rat.command.DeadlineCommand;
import rat.command.DeleteCommand;
import rat.command.EventCommand;
import rat.command.FindCommand;
import rat.command.ListCommand;
import rat.command.MarkCommand;
import rat.command.SnoozeCommand;
import rat.command.TodoCommand;
import rat.command.UnmarkCommand;

/**
 * Parses user input into executable commands.
 */
public class Parser {

    /**
     * Parses a raw user input line into a Command instance.
     *
     * Supported commands: bye, list, mark N, unmark N, todo DESC,
     * deadline DESC /by WHEN, event DESC /from WHEN /to WHEN,
     * delete N, find /on yyyy-MM-dd or find KEYWORD.
     */
    public static Command parse(String input) throws RatException {
        assert input != null : "Parser.parse expects non-null input";
        String trimmed = input.trim();
        if (trimmed.equalsIgnoreCase("bye")) return new ByeCommand();
        if (trimmed.equalsIgnoreCase("list")) return new ListCommand();
        if (trimmed.startsWith("mark")) return parseMarkCommand(trimmed);
        if (trimmed.startsWith("unmark")) return parseUnmarkCommand(trimmed);
        if (trimmed.startsWith("todo")) return new TodoCommand(trimmed.substring(4).trim());
        if (trimmed.startsWith("deadline")) return parseDeadlineCommand(trimmed);
        if (trimmed.startsWith("event")) return parseEventCommand(trimmed);
        if (trimmed.startsWith("delete")) return parseDeleteCommand(trimmed);
        if (trimmed.startsWith("find")) return parseFindCommand(trimmed);
        if (trimmed.startsWith("snooze")) return parseSnoozeCommand(trimmed);
        throw new RatException("I don't understand that command.");
    }

    private static Command parseMarkCommand(String trimmed) throws RatException {
        String[] parts = trimmed.split(" ");
        if (parts.length < 2) throw new RatException("Please provide a task number to mark.");
        assert parts.length >= 2 : "Mark command requires an index token";
        int index = Integer.parseInt(parts[1]) - 1;
        return new MarkCommand(index);
    }

    private static Command parseUnmarkCommand(String trimmed) throws RatException {
        String[] parts = trimmed.split(" ");
        if (parts.length < 2) throw new RatException("Please provide a task number to unmark.");
        assert parts.length >= 2 : "Unmark command requires an index token";
        int index = Integer.parseInt(parts[1]) - 1;
        return new UnmarkCommand(index);
    }

    private static Command parseDeadlineCommand(String trimmed) {
        String[] parts = trimmed.substring(8).split("/by", 2);
        assert parts.length > 0 : "Deadline command expects a description segment";
        String description = parts[0].trim();
        String by = parts.length > 1 ? parts[1].trim() : "unspecified";
        return new DeadlineCommand(description, by);
    }

    private static Command parseEventCommand(String trimmed) {
        String body = trimmed.substring(5);
        String description;
        String from = "unspecified";
        String to = "unspecified";
        if (body.contains("/from")) {
            String[] splitFrom = body.split("/from", 2);
            assert splitFrom.length > 0 : "Event command expects a description before /from";
            description = splitFrom[0].trim();
            String rest = splitFrom.length > 1 ? splitFrom[1] : "";
            if (rest.contains("/to")) {
                String[] splitTo = rest.split("/to", 2);
                assert splitTo.length > 0 : "Event command expects a start segment before /to";
                from = splitTo[0].trim();
                to = splitTo.length > 1 ? splitTo[1].trim() : "unspecified";
            } else {
                from = rest.trim();
            }
        } else {
            description = body.trim();
        }
        return new EventCommand(description, from, to);
    }

    private static Command parseDeleteCommand(String trimmed) throws RatException {
        String[] parts = trimmed.split(" ");
        if (parts.length < 2) throw new RatException("Please provide a task number to delete.");
        assert parts.length >= 2 : "Delete command requires an index token";
        int index = Integer.parseInt(parts[1]) - 1;
        return new DeleteCommand(index);
    }

    private static Command parseFindCommand(String trimmed) throws RatException {
        String args = trimmed.substring(4).trim();
        if (args.startsWith("/on") || args.contains("/on")) {
            String[] parts = args.split("/on", 2);
            if (parts.length < 2) {
                throw new RatException("Please provide a date to search for. Usage: find /on yyyy-MM-dd");
            }
            assert parts.length == 2 : "Find /on command should produce exactly two segments";
            String dateStr = parts[1].trim();
            LocalDate searchDate = LocalDate.parse(dateStr);
            return FindCommand.byDate(searchDate);
        }
        if (args.isEmpty()) {
            throw new RatException("Please provide a keyword to search for. Usage: find keyword");
        }
        return FindCommand.byKeyword(args);
    }

    private static Command parseSnoozeCommand(String trimmed) throws RatException {
        String body = trimmed.substring(6).trim();
        if (body.isEmpty()) {
            throw new RatException("Please provide a task number to snooze.");
        }
        String[] split = body.split(" ", 2);
        String indexToken = split[0];
        assert !indexToken.isBlank() : "Snooze command requires an index token";
        int index = Integer.parseInt(indexToken) - 1;
        String options = split.length > 1 ? split[1].trim() : "";
        if (options.isEmpty()) {
            throw new RatException("Please provide new schedule details using /by or /from and /to.");
        }

        String newBy = null;
        String newFrom = null;
        String newTo = null;

        if (options.startsWith("/by") || options.contains("/by")) {
            String[] bySplit = options.split("/by", 2);
            if (bySplit.length < 2) {
                throw new RatException("Please provide the new date/time after /by.");
            }
            newBy = bySplit[1].trim();
            if (newBy.isEmpty()) {
                throw new RatException("Please provide the new date/time after /by.");
            }
        } else {
            if (!options.contains("/from")) {
                throw new RatException("Events should specify /from and /to when snoozing.");
            }
            String[] fromSplit = options.split("/from", 2);
            if (fromSplit.length < 2) {
                throw new RatException("Please provide the new start time after /from.");
            }
            String rest = fromSplit[1];
            String[] toSplit = rest.split("/to", 2);
            if (toSplit.length < 2) {
                throw new RatException("Please provide the new end time after /to.");
            }
            newFrom = toSplit[0].trim();
            newTo = toSplit[1].trim();
            if (newFrom.isEmpty() || newTo.isEmpty()) {
                throw new RatException("Please provide both start and end times when snoozing an event.");
            }
        }

        return new SnoozeCommand(index, newBy, newFrom, newTo);
    }
}
