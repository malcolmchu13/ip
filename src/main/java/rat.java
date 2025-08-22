import java.util.Scanner;
public class rat {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("""
                Hello! I'm rat
                 What can I do for you?\s
                
                Bye. Hope to see you again soon!""");
        while (true) {
            String input = scanner.nextLine();   // read user command
            if (input.equals("bye")) {
                break;   // exit loop if user types "bye"
            }
            System.out.println("____________________________________________________________");
            System.out.println(" " + input);     // echo the command
            System.out.println("____________________________________________________________");
        }

        System.out.println("____________________________________________________________");
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }
}
