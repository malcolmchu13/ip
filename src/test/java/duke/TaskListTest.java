package duke;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TaskListTest {

    @Test
    void findTasksByDate_matchesDeadlinesAndEventsAcrossRanges() {
        TaskList list = new TaskList(new ArrayList<>());

        // Deadline on 2024-09-10 (midnight time by constructor default)
        list.add(new Deadline("return book", "2024-09-10"));

        // Event spanning two days: 2024-09-10 and 2024-09-11
        list.add(new Event("hackathon", "2024-09-10 1800", "2024-09-11 0100"));

        // Event outside the search date
        list.add(new Event("party", "2024-09-12 1900", "2024-09-12 2300"));

        var on10 = list.findTasksByDate(LocalDate.of(2024, 9, 10));
        assertEquals(2, on10.size());
        assertTrue(on10.get(0) instanceof Deadline || on10.get(1) instanceof Deadline);

        var on11 = list.findTasksByDate(LocalDate.of(2024, 9, 11));
        assertEquals(1, on11.size());
        assertTrue(on11.get(0) instanceof Event);

        var on12 = list.findTasksByDate(LocalDate.of(2024, 9, 12));
        assertEquals(1, on12.size());
        assertTrue(on12.get(0) instanceof Event);
    }

    @Test
    void toDisplayString_formatsListAndEmptyStates() {
        TaskList list = new TaskList();
        String emptyMsg = list.toDisplayString();
        assertTrue(emptyMsg.contains("No tasks added yet!"));

        list.add(new ToDo("read book"));
        String oneItem = list.toDisplayString();
        assertTrue(oneItem.contains("Here are the tasks in your list:"));
        assertTrue(oneItem.contains("1. [T][ ] read book"));
    }
}

