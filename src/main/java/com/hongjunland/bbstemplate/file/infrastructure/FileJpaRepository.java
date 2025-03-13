package com.hongjunland.bbstemplate.file.infrastructure;

import com.hongjunland.bbstemplate.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<File, Long> {
}
