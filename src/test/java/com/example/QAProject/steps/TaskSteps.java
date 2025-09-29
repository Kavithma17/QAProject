package com.example.QAProject.steps;

import com.example.QAProject.model.Task;
import com.example.QAProject.service.TaskService;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaskSteps {

    @Autowired
    private TaskService taskService;

    private Task task;
    private Task savedTask;
    private Exception exception;

    @Given("a task with title {string} and description {string}")
    public void a_task_with_title_and_description(String title, String description) {
        task = Task.builder().title(title).description(description).build();
        exception = null;
    }

    @When("the user adds the task")
    public void the_user_adds_the_task() {
        try {
            savedTask = taskService.addTask(task);
        } catch (Exception e) {
            exception = e;
        }
    }

    @Then("the task should be saved successfully")
    public void the_task_should_be_saved_successfully() {
        Assertions.assertNotNull(savedTask);
        Assertions.assertEquals(task.getTitle(), savedTask.getTitle());
    }

    @Then("the system should return an error {string}")
    public void the_system_should_return_an_error(String message) {
        Assertions.assertNotNull(exception);
        Assertions.assertEquals(message, exception.getMessage());
    }
}
