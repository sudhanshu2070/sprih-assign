package com.example.sprih;

import com.example.sprih.controller.TaskInMemoryController;
import com.example.sprih.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SprihApplicationTests {

    @Autowired
    private TaskInMemoryController taskController;

    @Autowired
    private TaskService taskService;

    @Test
    void contextLoads() {
    }

    // Example of testing if the controller and service beans are loaded
    @Test
    void controllerAndServiceShouldNotBeNull() {
        assert taskController != null;
        assert taskService != null;
    }
}