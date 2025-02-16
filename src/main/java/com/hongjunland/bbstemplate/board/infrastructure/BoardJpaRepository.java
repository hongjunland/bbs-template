package com.hongjunland.bbstemplate.board.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hongjunland.bbstemplate.board.domain.BoardJpaEntity;

public interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, Long> {
}
