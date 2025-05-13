package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Task;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TaskRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Task save(Task task) {
        entityManager.persist(task);
        return task;
    }

    public Optional<Task> findById(Long id) {
        Task task = entityManager.find(Task.class, id);
        return Optional.ofNullable(task);
    }

    public List<Task> findAll(int limit, int offset) {
        return entityManager.createQuery("SELECT t FROM Task t ORDER BY t.createdAt DESC", Task.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public void update(Task task) {
        entityManager.merge(task);
    }

    public void delete(Long id) {
        Task task = entityManager.find(Task.class, id);
        if (task != null) {
            entityManager.remove(task);
        }
    }
}
