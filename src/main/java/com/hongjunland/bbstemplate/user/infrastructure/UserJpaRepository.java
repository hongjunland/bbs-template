package com.hongjunland.bbstemplate.user.infrastructure;

import com.hongjunland.bbstemplate.user.domain.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
}
