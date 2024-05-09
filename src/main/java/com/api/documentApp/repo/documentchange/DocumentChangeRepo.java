package com.api.documentApp.repo.documentchange;

import com.api.documentApp.domain.entity.DocumentChangeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentChangeRepo extends JpaRepository<DocumentChangeEntity, Long> {
}
