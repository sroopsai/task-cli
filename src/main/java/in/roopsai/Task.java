package in.roopsai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


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


    public static void add(String description) {
        Task task = Task.builder().id(UUID.randomUUID()).description(description).status(Status.TODO).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        // Add it to the
        tasks.put(task.id(), task);
        task.saveToFile();
    }

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

    public static void markAsStatus(UUID taskId, Status status) {
        // check if task exists
        tasks.keySet().stream().filter(task -> task.equals(taskId)).findFirst().ifPresentOrElse(task -> {
            Task newTask = Task.builder().id(task).createdAt(tasks.get(task).createdAt()).updatedAt(LocalDateTime.now()).status(status).description(tasks.get(task).description()).build();
            tasks.put(newTask.id(), newTask);
            newTask.saveToFile();
        }, () -> System.out.println("Task with id " + taskId + " not found"));
    }

    private void saveToFile() {
        try {

            objectMapper.writeValue(new File(FILE_NAME), tasks.values());
        } catch (IOException e) {
            System.out.println("Error saving task to file: " + e.getMessage());
        }
    }


}
