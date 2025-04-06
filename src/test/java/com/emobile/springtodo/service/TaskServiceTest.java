package com.emobile.springtodo.service;

import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    private Task testTask;

    @BeforeEach
    public void setUp() {
        testTask = new Task(1L, "Тестовая задача", "Описание", false, LocalDateTime.now());
    }

    @Test
    public void testFindAll() {
        when(taskRepository.findAll(10, 0)).thenReturn(List.of(testTask));
        List<Task> tasks = taskService.findAll(10, 0);
        assertEquals(1, tasks.size());
        assertEquals("Тестовая задача", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll(10, 0);
    }

    @Test
    public void testFindById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        Optional<Task> foundTask = taskService.findById(1L);
        assertTrue(foundTask.isPresent());
        assertEquals("Тестовая задача", foundTask.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testSave() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        Task savedTask = taskService.save(new Task(null, "Новая задача", "Описание", false, null));
        assertNotNull(savedTask.getId());
        assertEquals("Тестовая задача", savedTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdate() {
        Task updatedTask = new Task(1L, "Обновлённая задача", "Новое описание", true, testTask.getCreatedAt());
        taskService.update(updatedTask);
        verify(taskRepository, times(1)).update(updatedTask);
    }

    @Test
    public void testDelete() {
        taskService.delete(1L);
        verify(taskRepository, times(1)).delete(1L);
    }

}