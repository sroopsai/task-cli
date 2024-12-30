package in.roopsai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Represents a task with its associated properties and provides methods for task management.
 *
 * @param id          The unique identifier of the task.
 * @param description The description of the task.
 * @param status      The current status of the task.
 * @param createdAt   The date and time when the task was created.
 * @param updatedAt   The date and time when the task was last updated.
 */
@Builder
public record Task(UUID id, String description, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {

    private static final String FILE_NAME = "tasks.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Map<UUID, Task> tasks = new HashMap<>();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    static {
        File taskFile = new File(FILE_NAME);
        if (taskFile.exists()) {
            taskFile.delete();
            try {
                taskFile.createNewFile();
            } catch (IOException e) {
                System.out.println("Unable to create file: " + e.getMessage());
            }

            try {
                tasks = objectMapper.readValue(taskFile, new TypeReference<>() {
                });
            } catch (IOException e) {
                System.out.println("Unable to read tasks from file: " + e.getMessage());
            }
        }
    }

    /**
     * Adds a new task with the given description.
     *
     * @param description The description of the task to be added.
     */
    public static void add(String description) {
        Task task = Task.builder().id(UUID.randomUUID()).description(description).status(Status.TODO).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        tasks.put(task.id(), task);
        task.saveToFile();
    }

    /**
     * Reads and prints all tasks from the file.
     */
    public static void readAllTasks() {
        File taskFile = new File(FILE_NAME);
        try {
            if (taskFile.exists()) {
                Collection<Task> tasks = objectMapper.readValue(taskFile, new TypeReference<>() {
                });

                tasks.forEach(System.out::println);
            }
        } catch (IOException e) {
            System.out.println("Error reading tasks from file: " + e.getMessage());
        }
    }

    /**
     * Reads and prints tasks with a specific status from the file.
     *
     * @param status The status of tasks to be read and printed.
     */
    public static void readTasksBasedOnStatus(Status status) {
        File taskFile = new File(FILE_NAME);
        try {
            if (taskFile.exists()) {
                List<Task> tasks = objectMapper.readValue(taskFile, new TypeReference<>() {
                });
                tasks.stream().filter(task -> task.status().equals(status)).forEach(System.out::println);
            }
        } catch (IOException e) {
            System.out.println("Error reading tasks from file: " + e.getMessage());
        }
    }

    /**
     * Updates the status of a task with the given ID.
     *
     * @param taskId The ID of the task to be updated.
     * @param status The new status to be set for the task.
     */
    public static void markAsStatus(UUID taskId, Status status) {
        tasks.keySet().stream().filter(task -> task.equals(taskId)).findFirst().ifPresentOrElse(task -> {
            Task newTask = Task.builder().id(task).createdAt(tasks.get(task).createdAt()).updatedAt(LocalDateTime.now()).status(status).description(tasks.get(task).description()).build();
            tasks.put(newTask.id(), newTask);
            newTask.saveToFile();
        }, () -> System.out.println("Task with id " + taskId + " not found"));
    }

    /**
     * Saves the current tasks to the file.
     */
    private void saveToFile() {
        try {
            objectMapper.writeValue(new File(FILE_NAME), tasks.values());
        } catch (IOException e) {
            System.out.println("Error saving task to file: " + e.getMessage());
        }
    }
}
