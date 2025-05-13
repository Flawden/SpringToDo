package com.emobile.springtodo.service;

import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return taskRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(offset, limit));
    }

    @Cacheable(value = "tasks", key = "#id")
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    @CachePut(value = "tasks", key = "#result.id")
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    @CachePut(value = "tasks", key = "#task.id")
    public Task update(Task task) {
        taskRepository.save(task);
        return task;
    }

    @Transactional
    @CacheEvict(value = "tasks", key = "#id")
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}