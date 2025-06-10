package com.example.sprih.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.sprih.model.Task;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class JsonFileStorageService {
    private static final String STORAGE_FILE = "tasks.json";
    private final ObjectMapper objectMapper;
    
    public JsonFileStorageService() {
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    public List<Task> readTasks() throws IOException {
        File file = new File(STORAGE_FILE);

        System.out.println("Reading tasks from: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("File does not exist.");
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, new TypeReference<List<Task>>() {});
    }
    
    public void writeTasks(List<Task> tasks) throws IOException {
        objectMapper.writeValue(new File(STORAGE_FILE), tasks);
    }
}