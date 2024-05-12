package com.api.documentApp.repo.task;

import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TaskRepo extends JpaRepository<TaskEntity, Long> {
    public List<TaskEntity> findAllByDocument(DocumentEntity document);
}
