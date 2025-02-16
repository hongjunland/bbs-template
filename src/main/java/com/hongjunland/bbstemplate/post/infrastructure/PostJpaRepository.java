package com.hongjunland.bbstemplate.post.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hongjunland.bbstemplate.post.domain.PostJpaEntity;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Long> {
    @Query("SELECT p FROM PostJpaEntity p JOIN FETCH p.board b WHERE b.id = :boardId")
    List<PostJpaEntity> findByBoardId(Long boardId);
}
