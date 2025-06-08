package com.emobile.springtodo.service;

import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task(1L, "Тестовая задача", "Описание", false, LocalDateTime.now());
    }

    @Test
    void testFindAll() {
        int limit = 10;
        int offset = 0;
        when(taskRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(offset, limit))).thenReturn(List.of(testTask));
        List<Task> tasks = taskService.findAll(limit, offset);
        assertEquals(1, tasks.size());
        assertEquals("Тестовая задача", tasks.getFirst().getTitle());
        verify(taskRepository, times(1)).findAllByOrderByCreatedAtDesc(PageRequest.of(offset, limit));
    }

    @Test
    void testFindById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        Optional<Task> foundTask = taskService.findById(1L);
        assertTrue(foundTask.isPresent());
        assertEquals("Тестовая задача", foundTask.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        Task savedTask = taskService.save(new Task(null, "Новая задача", "Описание", false, null));
        assertNotNull(savedTask.getId());
        assertEquals("Тестовая задача", savedTask.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdate() {
        Task updatedTask = new Task(1L, "Обновлённая задача", "Новое описание", true, testTask.getCreatedAt());
        taskService.update(updatedTask);
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    void testDelete() {
        taskService.delete(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

}