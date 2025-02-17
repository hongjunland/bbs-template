package com.hongjunland.bbstemplate.post.infrastructure;

import com.hongjunland.bbstemplate.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.board b WHERE b.id = :boardId")
    List<Post> findByBoardId(Long boardId);
}
