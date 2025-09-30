package com.example.QAProject.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty") // validation
    private String title;

    private String description;
    private boolean completed = false;
}