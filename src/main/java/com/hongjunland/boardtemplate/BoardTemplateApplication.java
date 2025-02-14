package com.hongjunland.boardtemplate;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class BoardTemplateApplication {
    @Generated
    public static void main(String[] args) {
        SpringApplication.run(BoardTemplateApplication.class, args);
    }

}
