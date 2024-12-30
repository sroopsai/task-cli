package in.roopsai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Builder
public record Task(int id, String description, Status status, LocalDateTime createdAt, LocalDateTime updatedAt) {

    private static final AtomicInteger idCounter = new AtomicInteger(0);

    private static final String FILE_NAME = "tasks.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    private static List<Task> tasks = new ArrayList<>();

    static {
        File taskFile = new File(FILE_NAME);
        try {
            if (taskFile.exists()) {
                tasks = objectMapper.readValue(taskFile, new TypeReference<>() {
                });
            }
        } catch (IOException e) {
            System.out.println("Error reading tasks from file: " + e.getMessage());
        }
    }




    public static void create(String description) {
        Task task = Task
                .builder()
                .id(idCounter.incrementAndGet())
                .description(description)
                .status(Status.TODO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Add it to the
        tasks.add(task);
        task.saveToFile();
    }

    public static void readAllTasks() {
        File taskFile = new File(FILE_NAME);
        try {
            if (taskFile.exists()) {
                List<Task> tasks = objectMapper.readValue(taskFile, new TypeReference<>() {
                });
                tasks.forEach(System.out::println);

            }
        } catch (IOException e) {
            System.out.println("Error reading tasks from file: " + e.getMessage());
        }

    }

    public static void clearAllTasks() {

    }


    private void saveToFile() {
        try {

            objectMapper.writeValue(new File(FILE_NAME), tasks);
        } catch (IOException e) {
            System.out.println("Error saving task to file: " + e.getMessage());
        }
    }


}
