package duke;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    void parse_mark_parsesIndexAsZeroBased() throws Exception {
        Parser.ParsedCommand cmd = Parser.parse("mark 2");
        assertEquals(Parser.CommandType.MARK, cmd.type);
        assertEquals(1, cmd.index); // zero-based index
    }

    @Test
    void parse_todo_withoutDescription_throws() {
        RatException e = assertThrows(RatException.class, () -> Parser.parse("todo"));
        assertTrue(e.getMessage().contains("description of a todo cannot be empty"));
    }

    @Test
    void parse_deadline_withBy_populatesFields() throws Exception {
        Parser.ParsedCommand cmd = Parser.parse("deadline submit report /by 2024-09-10 1800");
        assertEquals(Parser.CommandType.DEADLINE, cmd.type);
        assertEquals("submit report", cmd.description);
        assertEquals("2024-09-10 1800", cmd.by);
    }

    @Test
    void parse_event_withFromTo_populatesFields() throws Exception {
        Parser.ParsedCommand cmd = Parser.parse("event team sync /from 2024-09-10 0900 /to 2024-09-10 1000");
        assertEquals(Parser.CommandType.EVENT, cmd.type);
        assertEquals("team sync", cmd.description);
        assertEquals("2024-09-10 0900", cmd.from);
        assertEquals("2024-09-10 1000", cmd.to);
    }

    @Test
    void parse_find_withDate_parsesLocalDate() throws Exception {
        Parser.ParsedCommand cmd = Parser.parse("find /on 2024-09-10");
        assertEquals(Parser.CommandType.FIND, cmd.type);
        assertEquals(LocalDate.of(2024, 9, 10), cmd.date);
    }
}

