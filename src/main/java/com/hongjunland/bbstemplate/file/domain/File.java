package com.hongjunland.bbstemplate.file.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "files")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String fileType;         // MIME 타입 (image/png, video/mp4 등)
    private String originalFilename; // 원본 파일명
    private String storedFilename;   // UUID 기반 파일명
    private String fileUrl;          // GCS URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileUploadStatus fileUploadStatus; // 업로드 상태 (PENDING, SUCCESS, FAILED)

    public void updateFileUploadStatus(FileUploadStatus status){
        this.fileUploadStatus = status;
    }
}
