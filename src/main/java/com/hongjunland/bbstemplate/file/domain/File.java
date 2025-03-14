package com.hongjunland.bbstemplate.file.domain;

import com.hongjunland.bbstemplate.common.domain.BaseTimeEntity;
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
public class File extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(name = "file_type")
    private String fileType;         // MIME 타입 (image/png, video/mp4 등)

    @Column(name = "original_filename")
    private String originalFilename; // 원본 파일명

    @Column(name = "stored_filename")
    private String storedFilename;   // UUID 기반 파일명

    @Column(name = "file_url")
    private String fileUrl;          // GCS URL

    @Enumerated(EnumType.STRING)
    @Column(name = "file_upload_status", nullable = false)
    private FileUploadStatus fileUploadStatus; // 업로드 상태 (PENDING, SUCCESS, FAILED)

    public void updateFileUploadStatus(FileUploadStatus status){
        this.fileUploadStatus = status;
    }
}
