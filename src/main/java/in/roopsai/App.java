package in.roopsai;

public class App {

    public static void main(String[] args) {


        start:
        while (true) {
            String input = System.console().readLine();
            String[] entireCommand = input.trim().split(" ", 2);
            String command = entireCommand[0];
            String argument = entireCommand.length > 1 ? entireCommand[1] : "";
            try {
                switch (command) {
                    case "add" -> {
                        if (!argument.isEmpty()) {
                            Task.create(argument);
                        } else {
                            System.out.println("Please provide a description for the task.");
                        }
                    }
                    case "list" -> Task.readAllTasks();
                    case "exit" -> {
                        break start;
                    }
                    default -> System.out.println("Invalid command. Supported commands: add, list");

                }
            } catch (Exception e) {
                System.out.println("Unknown error: " + e.getMessage() + " Please try again. If you need help, type 'help'.");
            }

        }

    }
}
