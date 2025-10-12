package com.example.QAProject.service;

import com.example.QAProject.model.Task;
import com.example.QAProject.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

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

    // Add a new task with validation + XSS escaping
    public Task addTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        // Escape user-provided text to avoid stored XSS
        task.setTitle(HtmlUtils.htmlEscape(task.getTitle()));
        if (task.getDescription() != null) {
            task.setDescription(HtmlUtils.htmlEscape(task.getDescription()));
        }

        return taskRepository.save(task);
    }

    // Get a task by ID
    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    // Update a task with validation + XSS escaping
    public Task updateTask(Task task) {
        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        task.setTitle(HtmlUtils.htmlEscape(task.getTitle()));
        if (task.getDescription() != null) {
            task.setDescription(HtmlUtils.htmlEscape(task.getDescription()));
        }

        return taskRepository.save(task);
    }

    // Delete a task by ID
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // Search tasks by keyword (escape input to prevent XSS when reflecting back)
    public List<Task> searchTasks(String keyword) {
        if (keyword == null) {
            return taskRepository.findAll();
        }
        // Note: Html escaping here prevents reflected XSS if keyword is echoed back.
        String safeKeyword = HtmlUtils.htmlEscape(keyword);
        return taskRepository.findByTitleContainingIgnoreCase(safeKeyword);
    }
}
