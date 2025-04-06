package com.emobile.springtodo.service;

import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Cacheable(value = "tasks", key = "#limit + ':' + #offset")
    public List<Task> findAll(int limit, int offset) {
        return taskRepository.findAll(limit, offset);
    }

    @Cacheable(value = "tasks", key = "#id")
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @CachePut(value = "tasks", key = "#result.id")
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @CachePut(value = "tasks", key = "#task.id")
    public Task update(Task task) {
        taskRepository.update(task);
        return task;
    }

    @CacheEvict(value = "tasks", key = "#id")
    public void delete(Long id) {
        taskRepository.delete(id);
    }
}