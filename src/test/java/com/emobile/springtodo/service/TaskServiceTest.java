package com.emobile.springtodo.service;

import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskServiceTest {
    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @Test
    public void testFindAll() {
        List<Task> tasks = List.of(new Task(1L, "Task 1", "Desc", false, LocalDateTime.now()));
        when(taskRepository.findAll(10, 0)).thenReturn(tasks);
        assertEquals(tasks, taskService.findAll(10, 0));
    }
}