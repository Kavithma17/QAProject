Feature: Add Task
  As a user
  I want to add a new task
  So that I can track my work

  Scenario: Successfully add a task with valid title
    Given a task with title "Test Task" and description "Demo"
    When the user adds the task
    Then the task should be saved successfully

  Scenario: Fail to add a task with empty title
    Given a task with title "" and description "Demo"
    When the user adds the task
    Then the system should return an error "Title is required"
