package com.hongjunland.bbstemplate.user.infrastructure;

import com.hongjunland.bbstemplate.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {
}
