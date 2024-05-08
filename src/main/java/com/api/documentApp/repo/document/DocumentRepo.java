package com.api.documentApp.repo.document;

import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Document;
import java.util.List;
import java.util.Optional;

@Transactional
public interface DocumentRepo extends JpaRepository<DocumentEntity, String> {
    public List<DocumentEntity> findAllByUser(UserEntity user);
}
