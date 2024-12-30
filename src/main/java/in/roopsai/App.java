package in.roopsai;

import java.util.UUID;

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
                            Task.add(argument);
                        } else {
                            System.out.println("Please provide a description for the task.");
                        }
                    }
                    case "list" -> {
                        if (argument.isEmpty()) {
                            Task.readAllTasks();
                        } else {
                            Status status = Status.valueOf(argument.toUpperCase());
                            Task.readTasksBasedOnStatus(status);
                        }

                    }
                    case "mark-in-progress" -> {
                        if (argument.isEmpty()) {
                            System.out.println("Please provide the ID of the task to mark as in progress.");
                        } else {
                            Task.markAsStatus(UUID.fromString(argument), Status.IN_PROGRESS);
                        }
                    }

                    case "mark-done" -> {
                        if (argument.isEmpty()) {
                            System.out.println("Please provide the ID of the task to mark as done.");
                        } else {
                            Task.markAsStatus(UUID.fromString(argument), Status.COMPLETED);
                        }
                    }

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
