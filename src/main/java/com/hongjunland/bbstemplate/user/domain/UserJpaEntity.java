package com.hongjunland.bbstemplate.user.domain;

import com.hongjunland.bbstemplate.common.domain.BaseTimeEntity;
import com.hongjunland.bbstemplate.post.domain.PostJpaEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "users")
public class UserJpaEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;  // 사용자명
    private String email;     // 이메일

}