package com.example.QAProject.controller;
import com.example.QAProject.model.Task;
import com.example.QAProject.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

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

    // helper to create security headers for responses
    private HttpHeaders securityHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Frame-Options", "DENY"); // clickjacking protection
        headers.set("X-Content-Type-Options", "nosniff"); // prevent MIME sniffing
        headers.set("Content-Security-Policy", "default-src 'self'"); // basic CSP
        headers.set("Referrer-Policy", "no-referrer"); // reduce referrer leakage
        return headers;
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> list = taskService.getAllTasks();
        return ResponseEntity.ok().headers(securityHeaders()).body(list);
    }

    // Add a task (escape input and return with security headers)
    @PostMapping
    public ResponseEntity<?> addTask(@RequestBody Task task) {
        try {

            if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }
            if (task.getDescription() == null || task.getDescription().trim().isEmpty()) {
                throw new IllegalArgumentException("Description cannot be empty");
            }
            // 🔹 Length validation
            if (task.getTitle().length() > 50) {
                throw new IllegalArgumentException("Title too long (max 100 characters)");
            }
            if (task.getDescription().length() > 500) {
                throw new IllegalArgumentException("Description too long (max 500 characters)");
            }

            // Escape again at controller boundary (defense in depth)
            task.setTitle(HtmlUtils.htmlEscape(task.getTitle()));
            if (task.getDescription() != null) {
                task.setDescription(HtmlUtils.htmlEscape(task.getDescription()));
            }

            Task savedTask = taskService.addTask(task);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(securityHeaders())
                    .body(savedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .headers(securityHeaders())
                    .body(e.getMessage());
        }
    }

    // Update task
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Optional<Task> existing = taskService.getTask(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(securityHeaders())
                    .body("Task not found");
        }

        // Escape inputs
        task.setTitle(HtmlUtils.htmlEscape(task.getTitle()));
        if (task.getDescription() != null) {
            task.setDescription(HtmlUtils.htmlEscape(task.getDescription()));
        }

        task.setId(id);
        Task updated = taskService.updateTask(task);
        return ResponseEntity.ok().headers(securityHeaders()).body(updated);
    }

    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        Optional<Task> existing = taskService.getTask(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(securityHeaders())
                    .body("Task not found");
        }
        taskService.deleteTask(id);
        return ResponseEntity.ok().headers(securityHeaders()).body("Task deleted successfully");
    }

    // Search tasks (escape keyword and return results with headers)
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(@RequestParam(required = false) String keyword) {
        // Escape keyword to avoid reflected XSS if parameter is echoed later
        String safeKeyword = keyword == null ? null : HtmlUtils.htmlEscape(keyword);
        List<Task> tasks = taskService.searchTasks(safeKeyword);
        return ResponseEntity.ok().headers(securityHeaders()).body(tasks);
    }
}
