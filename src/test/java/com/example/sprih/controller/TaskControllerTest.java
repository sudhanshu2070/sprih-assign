package com.example.sprih.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.sprih.model.Priority;
import com.example.sprih.model.Status;
import com.example.sprih.model.Task;
import com.example.sprih.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
@Import(TaskController.class) 
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock  // Pure Mockito annotation
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task(
            1L,
            "Sample Task",
            "Description",
            LocalDate.now().plusDays(7),
            Priority.MEDIUM,
            Status.IN_PROGRESS
        );
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(sampleTask));

        mockMvc.perform(get("/api/tasks"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(sampleTask);

        mockMvc.perform(get("/api/tasks/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldCreateTask() throws Exception {
        when(taskService.createTask(any(Task.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Task"));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        when(taskService.updateTask(any(Long.class), any(Task.class))).thenReturn(sampleTask);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
               .andExpect(status().isNoContent());
    }
}