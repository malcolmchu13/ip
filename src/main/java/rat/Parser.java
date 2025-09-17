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
        if (trimmed.equalsIgnoreCase("bye")) {
            return new ByeCommand();
        } else if (trimmed.equalsIgnoreCase("list")) {
            return new ListCommand();
        } else if (trimmed.startsWith("mark")) {
            String[] parts = trimmed.split(" ");
            if (parts.length < 2) throw new RatException("Please provide a task number to mark.");
            assert parts.length >= 2 : "Mark command requires an index token";
            int index = Integer.parseInt(parts[1]) - 1;
            return new MarkCommand(index);
        } else if (trimmed.startsWith("unmark")) {
            String[] parts = trimmed.split(" ");
            if (parts.length < 2) throw new RatException("Please provide a task number to unmark.");
            assert parts.length >= 2 : "Unmark command requires an index token";
            int index = Integer.parseInt(parts[1]) - 1;
            return new UnmarkCommand(index);
        } else if (trimmed.startsWith("todo")) {
            String description = trimmed.substring(4).trim();
            return new TodoCommand(description);
        } else if (trimmed.startsWith("deadline")) {
            String[] parts = trimmed.substring(8).split("/by", 2);
            assert parts.length > 0 : "Deadline command expects a description segment";
            String description = parts[0].trim();
            String by = parts.length > 1 ? parts[1].trim() : "unspecified";
            return new DeadlineCommand(description, by);
        } else if (trimmed.startsWith("event")) {
            // Split once on /from, then on /to from the remaining
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
        } else if (trimmed.startsWith("delete")) {
            String[] parts = trimmed.split(" ");
            if (parts.length < 2) throw new RatException("Please provide a task number to delete.");
            assert parts.length >= 2 : "Delete command requires an index token";
            int index = Integer.parseInt(parts[1]) - 1;
            return new DeleteCommand(index);
        } else if (trimmed.startsWith("find")) {
            String args = trimmed.substring(4).trim();
            if (args.startsWith("/on") || args.contains("/on")) {
                String[] parts = args.split("/on", 2);
                if (parts.length < 2) throw new RatException("Please provide a date to search for. Usage: find /on yyyy-MM-dd");
                assert parts.length == 2 : "Find /on command should produce exactly two segments";
                String dateStr = parts[1].trim();
                LocalDate searchDate = LocalDate.parse(dateStr);
                return FindCommand.byDate(searchDate);
            } else {
                if (args.isEmpty()) throw new RatException("Please provide a keyword to search for. Usage: find keyword");
                return FindCommand.byKeyword(args);
            }
        }

        throw new RatException("I don't understand that command.");
    }
}
