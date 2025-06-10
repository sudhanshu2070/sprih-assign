package com.example.sprih.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sprih.exception.TaskNotFoundException;
import com.example.sprih.model.Priority;
import com.example.sprih.model.Status;
import com.example.sprih.model.Task;
import com.example.sprih.repository.InMemoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskInMemoryService {

    private final InMemoryRepository repository;

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task getTaskById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    public Task createTask(Task task) {
        validateTask(task);
        task.setStatus(task.getStatus() != null ? task.getStatus() : Status.PENDING); // default status
        return repository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task existingTask = getTaskById(id);

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

        return repository.save(existingTask);
    }

    public void deleteTask(Long id) {
        repository.deleteById(id);
    }

    public List<Task> filterTasks(Status status, Priority priority, LocalDate fromDate, LocalDate toDate) {
        return repository.findByCriteria(status, priority, fromDate, toDate);
    }

    private void validateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
    }
}