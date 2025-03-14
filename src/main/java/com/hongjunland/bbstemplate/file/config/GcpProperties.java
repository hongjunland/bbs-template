package com.hongjunland.bbstemplate.file.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.cloud.gcp")  // 👈 YAML의 해당 부분 매핑
public class GcpProperties {
    private Credentials credentials;
    private String projectId;
    private String bucket;

    @Getter
    @Setter
    public static class Credentials {
        private String location;
    }
}
