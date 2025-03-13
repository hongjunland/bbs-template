package com.hongjunland.bbstemplate.post.infrastructure;

import com.hongjunland.bbstemplate.post.domain.PostAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostAttachmentJpaRepository extends JpaRepository<PostAttachment, Long> {
}
