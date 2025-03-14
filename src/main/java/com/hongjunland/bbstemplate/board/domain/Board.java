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
    @Column(name = "board_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description",nullable = false)
    private String description;

    public void update(String name, String description){
        this.name = name;
        this.description = description;
    }

}
