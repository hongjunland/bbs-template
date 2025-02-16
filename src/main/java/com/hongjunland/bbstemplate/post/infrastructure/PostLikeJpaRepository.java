package com.hongjunland.bbstemplate.post.infrastructure;

import com.hongjunland.bbstemplate.post.domain.PostJpaEntity;
import com.hongjunland.bbstemplate.post.domain.PostLikeJpaEntity;
import com.hongjunland.bbstemplate.user.domain.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostLikeJpaRepository extends JpaRepository<PostLikeJpaEntity, Long> {
    Optional<PostLikeJpaEntity> findByPostAndUser(PostJpaEntity post, UserJpaEntity user);
    boolean existsByPostAndUser(PostJpaEntity post, UserJpaEntity user);
    void deleteByPostAndUser(PostJpaEntity post, UserJpaEntity user);

    int countByPost(PostJpaEntity post);
}
