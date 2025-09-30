package com.example.QAProject.controller;

import com.example.QAProject.model.Task;
import com.example.QAProject.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin("*")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        HttpHeaders headers = new HttpHeaders();
        addSecurityHeaders(headers); // ADDED security headers
        return ResponseEntity.ok().headers(headers).body(taskService.getAllTasks());
    }

    // Add a task
    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody Task task) {
        HttpHeaders headers = new HttpHeaders();
        addSecurityHeaders(headers); // ADDED security headers
        try {
            Task savedTask = taskService.addTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(savedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(e.getMessage());
        }
    }

    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {
        HttpHeaders headers = new HttpHeaders();
        addSecurityHeaders(headers); // ADDED security headers
        Optional<Task> existing = taskService.getTask(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body("Task not found");
        }
        task.setId(id);
        Task updated = taskService.updateTask(task);
        return ResponseEntity.ok().headers(headers).body(updated);
    }

    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        addSecurityHeaders(headers); // ADDED security headers
        Optional<Task> existing = taskService.getTask(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body("Task not found");
        }
        taskService.deleteTask(id);
        return ResponseEntity.ok().headers(headers).body("Task deleted successfully");
    }

    // Search tasks
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam String keyword) {
        HttpHeaders headers = new HttpHeaders();
        addSecurityHeaders(headers); // ADDED security headers
        List<Task> tasks = taskService.searchTasks(keyword);
        return ResponseEntity.ok().headers(headers).body(tasks);
    }

    // METHOD TO ADD BASIC SECURITY HEADERS
    private void addSecurityHeaders(HttpHeaders headers) {
        headers.add("X-Content-Type-Options", "nosniff");
        headers.add("X-Frame-Options", "DENY");
        headers.add("X-XSS-Protection", "1; mode=block");
        headers.add("Content-Security-Policy", "default-src 'self'");
    }
}
