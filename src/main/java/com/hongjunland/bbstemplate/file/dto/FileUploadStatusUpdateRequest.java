package com.hongjunland.bbstemplate.file.dto;


import com.hongjunland.bbstemplate.file.domain.FileUploadStatus;

public record FileUploadStatusUpdateRequest(Long fileId, FileUploadStatus status) {
}
