package com.hongjunland.boardtemplate.board.infrastructure;

import com.hongjunland.boardtemplate.board.domain.BoardJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {
}
