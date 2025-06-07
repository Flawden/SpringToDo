package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    private Task testTask;

    @BeforeEach
    public void setUp() {

        taskService.findAll(100, 0).forEach(task -> taskService.delete(task.getId()));

        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Тестовая задача");
        taskDto.setDescription("Описание тестовой задачи");
        taskDto.setCompleted(false);
        testTask = taskService.save(new Task(null, taskDto.getTitle(), taskDto.getDescription(), taskDto.isCompleted(), null));
    }

    @Test
    public void testGetAllTasks() throws Exception {
        mockMvc.perform(get("/api/tasks?limit=10&offset=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Тестовая задача")));
    }

    @Test
    public void testGetTaskById() throws Exception {
        mockMvc.perform(get("/api/tasks/" + testTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Тестовая задача")));
    }

    @Test
    public void testGetTaskByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTask() throws Exception {
        String taskJson = "{\"title\":\"Новая задача\",\"description\":\"Описание\",\"completed\":false}";
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Новая задача")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    public void testUpdateTask() throws Exception {
        String updatedTaskJson = "{\"title\":\"Обновлённая задача\",\"description\":\"Новое описание\",\"completed\":true}";
        mockMvc.perform(put("/api/tasks/" + testTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Обновлённая задача")))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    @Test
    public void testUpdateTaskNotFound() throws Exception {
        String updatedTaskJson = "{\"title\":\"Обновлённая задача\",\"description\":\"Новое описание\",\"completed\":true}";
        mockMvc.perform(put("/api/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTaskJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/api/tasks/" + testTask.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTaskNotFound() throws Exception {
        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound());
    }
}