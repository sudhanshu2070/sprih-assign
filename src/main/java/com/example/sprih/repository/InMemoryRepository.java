package com.example.sprih.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.sprih.exception.TaskNotFoundException;
import com.example.sprih.model.Priority;
import com.example.sprih.model.Status;
import com.example.sprih.model.Task;

@Service
public class InMemoryRepository {

    private final Map<Long, Task> taskMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Save or update a task.
     */
    public Task save(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        if (task.getId() == null || taskMap.get(task.getId()) == null && task.getId() > 0) {
            task.setId(idGenerator.getAndIncrement());
        }
        taskMap.put(task.getId(), task);
        return task;
    }

    /**
     * Find all tasks.
     */
    public List<Task> findAll() {
        return new ArrayList<>(taskMap.values());
    }

    /**
     * Find task by ID.
     */
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(taskMap.get(id));
    }

    /**
     * Delete task by ID.
     */
    public void deleteById(Long id) {
        boolean removed = taskMap.remove(id) != null;
        if (!removed) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
    }

    /**
     * Find tasks matching criteria.
     */
    public List<Task> findByCriteria(Status status, Priority priority, LocalDate fromDate, LocalDate toDate) {
        return taskMap.values().stream()
                .filter(task -> status == null || task.getStatus() == status)
                .filter(task -> priority == null || task.getPriority() == priority)
                .filter(task -> fromDate == null || 
                    (task.getDueDate() != null && !task.getDueDate().isBefore(fromDate)))
                .filter(task -> toDate == null || 
                    (task.getDueDate() != null && !task.getDueDate().isAfter(toDate)))
                .collect(Collectors.toList());
    }
}