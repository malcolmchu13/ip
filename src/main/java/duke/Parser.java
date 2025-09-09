package duke;

import java.time.LocalDate;

/**
 * Parses user input into structured commands.
 */
public class Parser {
    public enum CommandType { BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND }

    public static class ParsedCommand {
        public final CommandType type;
        public final String description;
        public final String by;
        public final String from;
        public final String to;
        public final Integer index; // 0-based index
        public final LocalDate date; // for find

        private ParsedCommand(CommandType type, String description, String by, String from, String to, Integer index, LocalDate date) {
            this.type = type; this.description = description; this.by = by; this.from = from; this.to = to; this.index = index; this.date = date;
        }

        public static ParsedCommand ofSimple(CommandType type) {
            return new ParsedCommand(type, null, null, null, null, null, null);
        }
    }

    public static ParsedCommand parse(String input) throws RatException {
        if (input.equalsIgnoreCase("bye")) {
            return ParsedCommand.ofSimple(CommandType.BYE);
        } else if (input.equalsIgnoreCase("list")) {
            return ParsedCommand.ofSimple(CommandType.LIST);
        } else if (input.startsWith("mark")) {
            String[] parts = input.split(" ");
            if (parts.length < 2) throw new RatException("Please provide a task number to mark.");
            int index = Integer.parseInt(parts[1]) - 1;
            return new ParsedCommand(CommandType.MARK, null, null, null, null, index, null);
        } else if (input.startsWith("unmark")) {
            String[] parts = input.split(" ");
            if (parts.length < 2) throw new RatException("Please provide a task number to unmark.");
            int index = Integer.parseInt(parts[1]) - 1;
            return new ParsedCommand(CommandType.UNMARK, null, null, null, null, index, null);
        } else if (input.startsWith("todo")) {
            String description = input.substring(4).trim();
            if (description.isEmpty()) throw new RatException("The description of a todo cannot be empty.");
            return new ParsedCommand(CommandType.TODO, description, null, null, null, null, null);
        } else if (input.startsWith("deadline")) {
            String[] parts = input.substring(8).split("/by", 2);
            String description = parts[0].trim();
            if (description.isEmpty()) throw new RatException("The description of a deadline cannot be empty.");
            String by = parts.length > 1 ? parts[1].trim() : "unspecified";
            return new ParsedCommand(CommandType.DEADLINE, description, by, null, null, null, null);
        } else if (input.startsWith("event")) {
            String[] parts = input.substring(5).split("/from|/to");
            String description = parts[0].trim();
            if (description.isEmpty()) throw new RatException("The description of an event cannot be empty.");
            String from = (parts.length > 1) ? parts[1].trim() : "unspecified";
            String to = (parts.length > 2) ? parts[2].trim() : "unspecified";
            return new ParsedCommand(CommandType.EVENT, description, null, from, to, null, null);
        } else if (input.startsWith("delete")) {
            String[] parts = input.split(" ");
            if (parts.length < 2) throw new RatException("Please provide a task number to delete.");
            int index = Integer.parseInt(parts[1]) - 1;
            return new ParsedCommand(CommandType.DELETE, null, null, null, null, index, null);
        } else if (input.startsWith("find")) {
            String args = input.substring(4).trim();
            // Support both: keyword search (find <keyword>) and date search (find /on yyyy-MM-dd)
            if (args.startsWith("/on") || args.contains("/on")) {
                String[] parts = args.split("/on", 2);
                if (parts.length < 2) throw new RatException("Please provide a date to search for. Usage: find /on yyyy-MM-dd");
                String dateStr = parts[1].trim();
                LocalDate searchDate = LocalDate.parse(dateStr);
                return new ParsedCommand(CommandType.FIND, null, null, null, null, null, searchDate);
            } else {
                if (args.isEmpty()) throw new RatException("Please provide a keyword to search for. Usage: find keyword");
                return new ParsedCommand(CommandType.FIND, args, null, null, null, null, null);
            }
        }

        throw new RatException("I don't understand that command.");
    }
}
