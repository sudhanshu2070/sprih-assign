package com.example.sprih.repository;

import com.example.sprih.model.Priority;
import com.example.sprih.model.Status;
import com.example.sprih.model.Task;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRepository {

    private final Map<Long, Task> tasks = new HashMap<>();
    private long idCounter = 1;

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idCounter++);
        }
        tasks.put(task.getId(), task);
        return task;
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteById(Long id) {
        tasks.remove(id);
    }

    public List<Task> findByCriteria(Status status, Priority priority, LocalDate fromDate, LocalDate toDate) {
        return tasks.values().stream()
                .filter(task -> (status == null || task.getStatus() == status) &&
                                (priority == null || task.getPriority() == priority) &&
                                (fromDate == null || !task.getDueDate().isBefore(fromDate)) &&
                                (toDate == null || !task.getDueDate().isAfter(toDate)))
                .collect(Collectors.toList());
    }
}
