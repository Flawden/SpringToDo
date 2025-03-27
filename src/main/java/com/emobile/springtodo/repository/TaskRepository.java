package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Task;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TaskRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TaskRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Task save(Task task) {
        String sql = "INSERT INTO tasks (title, description, completed) VALUES (:title, :description, :completed) RETURNING *";
        Map<String, Object> params = Map.of(
                "title", task.getTitle(),
                "description", task.getDescription(),
                "completed", task.isCompleted()
        );
        return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Task.class));
    }

    public Optional<Task> findById(Long id) {
        String sql = "SELECT * FROM tasks WHERE id = :id";
        return jdbcTemplate.query(sql, Map.of("id", id), new BeanPropertyRowMapper<>(Task.class))
                .stream().findFirst();
    }

    public List<Task> findAll(int limit, int offset) {
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC LIMIT :limit OFFSET :offset";
        Map<String, Integer> params = Map.of("limit", limit, "offset", offset);
        return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(Task.class));
    }

    @Transactional
    public void update(Task task) {
        String sql = "UPDATE tasks SET title = :title, description = :description, completed = :completed WHERE id = :id";
        Map<String, Object> params = Map.of(
                "title", task.getTitle(),
                "description", task.getDescription(),
                "completed", task.isCompleted(),
                "id", task.getId()
        );
        jdbcTemplate.update(sql, params);
    }

    @Transactional
    public void delete(Long id) {
        String sql = "DELETE FROM tasks WHERE id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }
}