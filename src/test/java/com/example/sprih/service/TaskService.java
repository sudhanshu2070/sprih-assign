package com.example.sprih.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.sprih.exception.TaskNotFoundException;
import com.example.sprih.model.Task;
import com.example.sprih.model.Priority;
import com.example.sprih.model.Status;
import com.example.sprih.repository.InMemoryRepository;

@Service
public class TaskService {

    private final InMemoryRepository repository;

    public TaskService(InMemoryRepository repository) {
        this.repository = repository;
    }

    public Task createTask(Task task) throws IOException {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        return repository.save(task);
    }

    public Task getTaskById(Long id) throws IOException {
        return repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    public Task updateTask(Long id, Task update) throws IOException {
        Task existing = repository.findById(id)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));

        if (update.getTitle() != null && !update.getTitle().trim().isEmpty()) {
            existing.setTitle(update.getTitle());
        }
        if (update.getDescription() != null) {
            existing.setDescription(update.getDescription());
        }
        if (update.getDueDate() != null) {
            existing.setDueDate(update.getDueDate());
        }
        if (update.getPriority() != null) {
            existing.setPriority(update.getPriority());
        }
        if (update.getStatus() != null) {
            existing.setStatus(update.getStatus());
        }

        return repository.save(existing);
    }

    public void deleteTask(Long id) throws IOException {
        if (!repository.findById(id).isPresent()) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public List<Task> filterTasks(Status status, Priority priority, LocalDate fromDate, LocalDate toDate) throws IOException {
        return repository.findByCriteria(status, priority, fromDate, toDate);
    }

    public List<Task> getAllTasks() throws IOException {
        return repository.findByCriteria(null, null, null, null);
    }
}