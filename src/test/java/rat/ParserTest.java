package rat;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import rat.command.Command;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private static Storage tempStorage() throws Exception {
        Path tmp = Files.createTempFile("rat-test", ".txt");
        return new Storage(tmp.toString());
    }

    @Test
    void parse_mark_parsesIndexAsZeroBased() throws Exception {
        TaskList tasks = new TaskList();
        tasks.add(new ToDo("t1"));
        tasks.add(new ToDo("t2"));
        Command cmd = Parser.parse("mark 2");
        String res = cmd.execute(tasks, new Ui(), tempStorage());
        assertEquals("X", tasks.get(1).getStatusIcon()); // second task marked
        assertEquals(" ", tasks.get(0).getStatusIcon()); // first unchanged
        assertTrue(res.contains("marked this task as done"));
    }

    @Test
    void parse_todo_withoutDescription_throws() throws Exception {
        TaskList tasks = new TaskList();
        Command cmd = Parser.parse("todo");
        RatException e = assertThrows(RatException.class, () -> cmd.execute(tasks, new Ui(), tempStorage()));
        assertTrue(e.getMessage().toLowerCase().contains("description of a todo cannot be empty"));
    }

    @Test
    void parse_deadline_withBy_addsDeadlineTask() throws Exception {
        TaskList tasks = new TaskList();
        Command cmd = Parser.parse("deadline submit report /by 2024-09-10 1800");
        String resp = cmd.execute(tasks, new Ui(), tempStorage());
        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Deadline);
        assertTrue(resp.contains("I've added this task"));
    }

    @Test
    void parse_event_withFromTo_addsEventTask() throws Exception {
        TaskList tasks = new TaskList();
        Command cmd = Parser.parse("event team sync /from 2024-09-10 0900 /to 2024-09-10 1000");
        String resp = cmd.execute(tasks, new Ui(), tempStorage());
        assertEquals(1, tasks.size());
        assertTrue(tasks.get(0) instanceof Event);
        assertTrue(resp.contains("I've added this task"));
    }

    @Test
    void parse_find_withDate_filtersByDate() throws Exception {
        TaskList tasks = new TaskList();
        // Seed tasks
        Parser.parse("deadline submit report /by 2024-09-10 1800").execute(tasks, new Ui(), tempStorage());
        Parser.parse("event team sync /from 2024-09-10 0900 /to 2024-09-10 1000").execute(tasks, new Ui(), tempStorage());
        Parser.parse("todo something else").execute(tasks, new Ui(), tempStorage());

        Command find = Parser.parse("find /on 2024-09-10");
        String resp = find.execute(tasks, new Ui(), tempStorage());
        assertTrue(resp.contains("submit report"));
        assertTrue(resp.contains("team sync"));
    }
}
