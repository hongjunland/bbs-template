package com.hongjunland.bbstemplate.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileUploadStatus {
    PENDING("PENDING"),   // (1) 업로드 대기 (Signed URL 발급 후)
    SUCCESS("SUCCESS"),   // (2) 업로드 성공 (클라이언트가 서버에 성공 응답 보냄)
    FAILED("FAILED");     // (3) 업로드 실패 (클라이언트가 실패 응답 보냄)
    private final String description;

}
