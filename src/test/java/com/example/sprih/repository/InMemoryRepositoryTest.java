package com.example.sprih.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.sprih.model.Priority;
import com.example.sprih.model.Status;
import com.example.sprih.model.Task;

class InMemoryRepositoryTest {

    private InMemoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository();
    }

    @Test
    void shouldSaveAndFindTaskById() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setStatus(Status.PENDING);
        task.setPriority(Priority.HIGH);
        task.setDueDate(LocalDate.now());

        Task saved = repository.save(task);

        Task found = repository.findById(saved.getId())
                .orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.getTitle()).isEqualTo("Test Task");
    }

    @Test
    void shouldReturnAllTasks() {
        repository.save(createTask("Task 1", Priority.LOW));
        repository.save(createTask("Task 2", Priority.MEDIUM));

        List<Task> tasks = repository.findAll();

        assertThat(tasks).hasSize(2);
    }

    @Test
    void shouldDeleteTaskById() {
        Task task = repository.save(createTask("To Delete", Priority.HIGH));
        repository.deleteById(task.getId());

        assertThat(repository.findById(task.getId())).isEmpty();
    }

    @Test
    void shouldFilterTasksByStatus() {
        repository.save(createTask("Pending Task", Status.PENDING, Priority.LOW));
        repository.save(createTask("Completed Task", Status.COMPLETED, Priority.HIGH));

        List<Task> result = repository.findByCriteria(Status.PENDING, null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(Status.PENDING);
    }

    // Helper method
    private Task createTask(String title, Priority priority) {
        return createTask(title, Status.PENDING, priority);
    }

    private Task createTask(String title, Status status, Priority priority) {
        Task task = new Task();
        task.setTitle(title);
        task.setStatus(status);
        task.setPriority(priority);
        task.setDueDate(LocalDate.now().plusDays(1));
        return task;
    }
}