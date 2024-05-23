package com.api.documentApp.repo.documentgroup;

import com.api.documentApp.domain.entity.DocumentGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DocumentGroupRepo extends JpaRepository<DocumentGroupEntity, Long> {
}
