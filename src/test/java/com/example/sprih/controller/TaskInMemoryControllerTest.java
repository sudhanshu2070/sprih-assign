package com.example.sprih.controller;

import com.example.sprih.model.Priority;
import com.example.sprih.model.Status;
import com.example.sprih.model.Task;
import com.example.sprih.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskInMemoryControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskInMemoryController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setStatus(Status.PENDING);
        task1.setPriority(Priority.HIGH);
        task1.setDueDate(LocalDate.now());

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setStatus(Status.COMPLETED);
        task2.setPriority(Priority.LOW);
        task2.setDueDate(LocalDate.now());

        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskService.filterTasks(any(), any(), any(), any())).thenReturn(tasks);

        mockMvc.perform(get("/api/inMemory/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].status").value("COMPLETED"));
    }

    @Test
    public void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setStatus(Status.PENDING);
        task.setPriority(Priority.HIGH);
        task.setDueDate(LocalDate.now());
        
        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/inMemory/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
public void testCreateTask() throws Exception {
    Task task = new Task();
    task.setId(1L);
    task.setTitle("Task 1");
    task.setStatus(Status.PENDING);
    task.setPriority(Priority.HIGH);
    task.setDueDate(LocalDate.now().plusDays(1));  // Ensure future date

    when(taskService.createTask(any(Task.class))).thenReturn(task);

    // Correct JSON body and test status code
    mockMvc.perform(post("/api/inMemory/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\": \"Task 1\", \"status\": \"PENDING\", \"priority\": \"HIGH\", \"dueDate\": \"" + LocalDate.now().plusDays(1) + "\"}"))
            .andExpect(status().isCreated()) // Ensure it's 201 CREATED
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Task 1"))
            .andExpect(jsonPath("$.status").value("PENDING"));
}

@Test
public void testUpdateTask() throws Exception {
    Task task = new Task();
    task.setId(1L);
    task.setTitle("Updated Task");
    task.setStatus(Status.IN_PROGRESS);
    task.setPriority(Priority.MEDIUM);
    task.setDueDate(LocalDate.now().plusDays(1));  // Ensure future date

    when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(task);

    // Correct JSON body and test status code
    mockMvc.perform(put("/api/inMemory/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"title\": \"Updated Task\", \"status\": \"IN_PROGRESS\", \"priority\": \"MEDIUM\", \"dueDate\": \"" + LocalDate.now().plusDays(1) + "\"}"))
            .andExpect(status().isOk()) // Ensure it's 200 OK
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Updated Task"))
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
}


    @Test
    public void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/inMemory/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
