package com.hongjunland.bbstemplate.board.domain;

import com.hongjunland.bbstemplate.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "boards")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    public void update(String name, String description){
        this.name = name;
        this.description = description;
    }

    /**
     * 게시판 생성 정적 팩토리 메서드
     */
    @Builder
    public static Board create(String name, String description) {
        // 유효성 검증 등 도메인 규칙 추가 가능
        return Board.builder()
                .name(name)
                .description(description)
                .build();
    }
}
