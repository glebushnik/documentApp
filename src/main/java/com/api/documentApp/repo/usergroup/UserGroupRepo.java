package com.api.documentApp.repo.usergroup;

import com.api.documentApp.domain.entity.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepo extends JpaRepository<UserGroupEntity, Long> {
}
