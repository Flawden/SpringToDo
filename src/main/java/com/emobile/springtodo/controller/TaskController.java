package com.emobile.springtodo.controller;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Получить все задачи", description = "Возвращает список задач с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно")
    })
    public List<Task> getAllTasks(@RequestParam(defaultValue = "10") int limit,
                                  @RequestParam(defaultValue = "0") int offset) {
        logger.info("Запрос на получение всех задач с limit={} и offset={}", limit, offset);
        return taskService.findAll(limit, offset);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по её ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        logger.info("Запрос на получение задачи с id={}", id);
        return taskService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Задача с id={} не найдена", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @Operation(summary = "Создать новую задачу", description = "Создаёт новую задачу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача создана")
    })
    public Task createTask(@Valid @RequestBody TaskDto taskDto) {
        logger.info("Создание новой задачи: {}", taskDto);
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCompleted(taskDto.isCompleted());
        return taskService.save(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу", description = "Обновляет существующую задачу по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача обновлена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        logger.info("Обновление задачи с id={}: {}", id, taskDto);
        Optional<Task> existingTask = taskService.findById(id);
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setCompleted(taskDto.isCompleted());
            taskService.update(task);
            return ResponseEntity.ok(task);
        } else {
            logger.warn("Задача с id={} не найдена для обновления", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Задача удалена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("Удаление задачи с id={}", id);
        if (taskService.findById(id).isPresent()) {
            taskService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Задача с id={} не найдена для удаления", id);
            return ResponseEntity.notFound().build();
        }
    }
}