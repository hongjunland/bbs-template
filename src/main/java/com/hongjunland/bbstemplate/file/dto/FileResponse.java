package com.hongjunland.bbstemplate.file.dto;

import com.hongjunland.bbstemplate.file.domain.FileUploadStatus;
import lombok.Builder;

@Builder
public record FileResponse(
        Long fileId,
        String fileName,
        String fileUrl,
        String fileType,
        FileUploadStatus status
) {
}

