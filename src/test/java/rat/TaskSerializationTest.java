package rat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskSerializationTest {

    @Test
    void fromString_todo_roundTrips() throws Exception {
        Task t = Task.fromString("T | 1 | read book");
        assertTrue(t instanceof ToDo);
        assertEquals("T | 1 | read book", t.toFileString());
        assertTrue(t.toString().contains("[T][X]"));
    }

    @Test
    void fromString_deadline_roundTripsIso() throws Exception {
        Task t = Task.fromString("D | 0 | return book | 2024-09-10T18:00");
        assertTrue(t instanceof Deadline);
        assertEquals("D | 0 | return book | 2024-09-10T18:00", t.toFileString());
        // Human-readable string should contain formatted date
        assertTrue(t.toString().contains("[D][ ]"));
        assertTrue(t.toString().contains("by:"));
    }

    @Test
    void fromString_event_roundTripsIso() throws Exception {
        Task t = Task.fromString("E | 1 | conference | 2024-09-10T09:00 | 2024-09-10T17:00");
        assertTrue(t instanceof Event);
        assertEquals("E | 1 | conference | 2024-09-10T09:00 | 2024-09-10T17:00", t.toFileString());
        assertTrue(t.toString().contains("[E][X]"));
        assertTrue(t.toString().contains("from:"));
        assertTrue(t.toString().contains("to:"));
    }
}
