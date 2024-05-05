package com.api.documentApp.repo.document;

import com.api.documentApp.domain.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepo extends JpaRepository<DocumentEntity, String> {
}
