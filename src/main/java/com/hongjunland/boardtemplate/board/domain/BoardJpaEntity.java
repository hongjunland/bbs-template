package com.hongjunland.boardtemplate.board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "boards")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class BoardJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    public void update(String name, String description){
        this.name = name;
        this.description = description;
    }
}
