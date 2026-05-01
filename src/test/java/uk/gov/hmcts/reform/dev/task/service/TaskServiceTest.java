package uk.gov.hmcts.reform.dev.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.dev.task.exception.TaskNotFoundException;
import uk.gov.hmcts.reform.dev.task.model.Task;
import uk.gov.hmcts.reform.dev.task.model.TaskStatus;
import uk.gov.hmcts.reform.dev.task.repository.TaskRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    void shouldReturnAllTasks() {
        Task task = Task.builder()
            .id(1L)
            .title("Test task")
            .status(TaskStatus.TODO)
            .dueDate(LocalDate.now().plusDays(1))
            .build();

        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Test task", result.getFirst().getTitle());
    }

    @Test
    void shouldReturnTaskById() {
        Task task = Task.builder()
            .id(1L)
            .title("Test task")
            .status(TaskStatus.TODO)
            .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Test task", result.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(99L));
    }

    @Test
    void shouldCreateTaskWithDefaultStatusWhenStatusIsMissing() {
        Task task = Task.builder()
            .title("New task")
            .build();

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        assertEquals(TaskStatus.TODO, result.getStatus());
        verify(taskRepository).save(task);
    }

    @Test
    void shouldUpdateTask() {
        Task existingTask = Task.builder()
            .id(1L)
            .title("Old title")
            .description("Old description")
            .status(TaskStatus.TODO)
            .dueDate(LocalDate.now().plusDays(1))
            .build();

        Task updatedDetails = Task.builder()
            .title("New title")
            .description("New description")
            .status(TaskStatus.IN_PROGRESS)
            .dueDate(LocalDate.now().plusDays(2))
            .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedDetails);

        assertEquals("New title", result.getTitle());
        assertEquals("New description", result.getDescription());
        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
    }

    @Test
    void shouldUpdateTaskStatus() {
        Task existingTask = Task.builder()
            .id(1L)
            .title("Task")
            .status(TaskStatus.TODO)
            .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task result = taskService.updateTaskStatus(1L, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, result.getStatus());
    }

    @Test
    void shouldDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingTask() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(99L));

        verify(taskRepository, never()).deleteById(99L);
    }
}
