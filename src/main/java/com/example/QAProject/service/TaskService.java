package com.example.QAProject.service;

import com.example.QAProject.model.Task;
import com.example.QAProject.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils; // ADDED for XSS escaping

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

//    // Add a new task with validation and XSS prevention
//    public Task addTask(Task task) {
//        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
//            throw new IllegalArgumentException("Title is required");
//        }
//
//        // FIXED XSS: escape HTML characters to prevent script injection
//        task.setTitle(HtmlUtils.htmlEscape(task.getTitle()));
//        task.setDescription(HtmlUtils.htmlEscape(task.getDescription()));
//
//        // Safe from SQL injection since using repository
//        return taskRepository.save(task);
//    }

    public Task addTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        return taskRepository.save(task);
    }


    // Get a task by ID
    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    // Update a task with XSS prevention
    public Task updateTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        // FIXED XSS: escape HTML characters
        task.setTitle(HtmlUtils.htmlEscape(task.getTitle()));
        task.setDescription(HtmlUtils.htmlEscape(task.getDescription()));

        return taskRepository.save(task);
    }

    // Delete a task by ID
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // Search tasks by keyword
    public List<Task> searchTasks(String keyword) {
        // SAFE: repository method handles parameter binding
        return taskRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
