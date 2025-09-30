package com.example.QAProject.service;

import com.example.QAProject.model.Task;
import com.example.QAProject.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void addTask_shouldThrowException_whenTitleIsEmpty() {
        Task task = Task.builder().title("").description("desc").build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.addTask(task);
        });

        assertEquals("Title is required", exception.getMessage());
    }


    @Test
    void addTask_shouldSaveTask_whenTitleIsValid() {
        Task task = Task.builder().title("Test Task").description("desc").build();
        when(taskRepository.save(task)).thenReturn(task);

        Task saved = taskService.addTask(task);

        assertNotNull(saved);
        assertEquals("Test Task", saved.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getAllTasks_shouldReturnListOfTasks() {
        Task task1 = Task.builder().title("Task 1").build();
        Task task2 = Task.builder().title("Task 2").build();
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }
}
