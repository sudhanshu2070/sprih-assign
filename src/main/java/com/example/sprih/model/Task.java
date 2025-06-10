package com.example.sprih.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private Long id;
    
    @NotBlank(message = "Title is mandatory")
    private String title;
    
    private String description;
    
    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDate dueDate;
    
    @NotNull(message = "Priority is mandatory")
    private Priority priority;
    
    private Status status = Status.PENDING;
}