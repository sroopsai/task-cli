# Task CLI is a simple command-line interface application for managing tasks. It allows users to add, view, and update tasks with ease.

## Features

- Add new tasks
- View all tasks
- View tasks filtered by status
- Update task status
- Persistent storage of tasks in JSON format

## Prerequisites

- Java 17 or higher
- Maven (for building the project)

## Building the Project

To build the project, run the following command in the project root directory:

```bash
mvn clean package
```

### Running the Application

After building the project, you can run the application using the following command:

```bash
java -jar target/task-cli-1.0-SNAPSHOT.jar
```

### Usage

The application supports the following commands:

1. Add a new task: ```add "Task description"```
2. View all tasks: ```list```
3. View tasks filtered by status: ```list <status>```
4. Mark A task as In_Progress: ```mark-in-progress <task_id>```
5. Mark a task as Done: ```mark-done <task_id>```
6. Delete a task: ```delete <task_id>```

Replace `<task_id>` with the ID of the task you want to update.

### File Storage

Tasks are stored in a file named tasks.json in the same directory as the JAR file. This file is automatically created and managed by the application.

### Contributing

Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

### License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
