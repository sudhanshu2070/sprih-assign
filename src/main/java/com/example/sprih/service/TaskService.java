package com.example.sprih.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.sprih.exception.TaskNotFoundException;
import com.example.sprih.model.Priority;
import com.example.sprih.model.Status;
import com.example.sprih.model.Task;
import com.example.sprih.repository.JsonFileStorageService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final JsonFileStorageService storageService;
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    @PostConstruct
    public void init() throws IOException {
        List<Task> tasks = storageService.readTasks();
        if (!tasks.isEmpty()) {
            long maxId = tasks.stream()
                .mapToLong(Task::getId)
                .max()
                .orElse(0);
            idGenerator.set(maxId + 1);
        }
    }
    
    public List<Task> getAllTasks() throws IOException {
        return storageService.readTasks();
    }
    
    public Task getTaskById(Long id) throws IOException {
        return storageService.readTasks().stream()
            .filter(task -> task.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }
    
    public Task createTask(Task task) throws IOException {
        List<Task> tasks = storageService.readTasks();
        task.setId(idGenerator.getAndIncrement());
        task.setStatus(Status.PENDING); // Default status
        tasks.add(task);
        storageService.writeTasks(tasks);
        return task;
    }
    
    public Task updateTask(Long id, Task taskDetails) throws IOException {
        List<Task> tasks = storageService.readTasks();
        Task existingTask = tasks.stream()
            .filter(task -> task.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
        
        // Updating fields if they're provided in taskDetails
        if (taskDetails.getTitle() != null) {
            existingTask.setTitle(taskDetails.getTitle());
        }
        if (taskDetails.getDescription() != null) {
            existingTask.setDescription(taskDetails.getDescription());
        }
        if (taskDetails.getDueDate() != null) {
            existingTask.setDueDate(taskDetails.getDueDate());
        }
        if (taskDetails.getPriority() != null) {
            existingTask.setPriority(taskDetails.getPriority());
        }
        if (taskDetails.getStatus() != null) {
            existingTask.setStatus(taskDetails.getStatus());
        }
        
        storageService.writeTasks(tasks);
        return existingTask;
    }
    
    public void deleteTask(Long id) throws IOException {
        List<Task> tasks = storageService.readTasks();
        boolean removed = tasks.removeIf(task -> task.getId().equals(id));
        if (!removed) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        storageService.writeTasks(tasks);
    }
    
    public List<Task> filterTasks(Status status, Priority priority, 
                                LocalDate fromDate, LocalDate toDate) throws IOException {
        return storageService.readTasks().stream()
            .filter(task -> status == null || task.getStatus() == status)
            .filter(task -> priority == null || task.getPriority() == priority)
            .filter(task -> fromDate == null || 
                (task.getDueDate() != null && !task.getDueDate().isBefore(fromDate)))
            .filter(task -> toDate == null || 
                (task.getDueDate() != null && !task.getDueDate().isAfter(toDate)))
            .collect(Collectors.toList());
    }
}