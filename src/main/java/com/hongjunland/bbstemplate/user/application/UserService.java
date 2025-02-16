package com.hongjunland.bbstemplate.user.application;

import com.hongjunland.bbstemplate.user.domain.UserJpaEntity;
import com.hongjunland.bbstemplate.user.infrastructure.UserJpaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;

    @PostConstruct
    public void initUsers() {
        if (userJpaRepository.count() == 0) {
            userJpaRepository.save(UserJpaEntity.builder().username("홍길동").email("hong@example.com").build());
            userJpaRepository.save(UserJpaEntity.builder().username("김철수").email("kim@example.com").build());
        }
    }
}