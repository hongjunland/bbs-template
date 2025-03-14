package com.hongjunland.bbstemplate.file.presentation;

import com.hongjunland.bbstemplate.file.application.SignedUrlService;
import com.hongjunland.bbstemplate.file.dto.FileUploadStatusUpdateRequest;
import com.hongjunland.bbstemplate.file.dto.SignedUrlRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final SignedUrlService signedUrlService;

    @PostMapping("/signed-url/upload")
    public ResponseEntity<?> generateUpLoadSignedUrl(@Valid @RequestBody SignedUrlRequest signedUrlRequest) {
        return ResponseEntity.ok(signedUrlService.generateUploadSignedUrl(signedUrlRequest));
    }
    @PostMapping("/signed-url/download")
    public ResponseEntity<?> generateDownLoadSignedUrl(@RequestBody SignedUrlRequest signedUrlRequest) {
        return ResponseEntity.ok(signedUrlService.generateDownloadSignedUrl( signedUrlRequest));
    }

    @PutMapping("/upload-status")
    public ResponseEntity<?> updateFileUploadStatus(@RequestBody FileUploadStatusUpdateRequest fileUploadStatusUpdateRequest){
        signedUrlService.updateFileUploadStatus(fileUploadStatusUpdateRequest);
        return ResponseEntity.ok(null);
    }
    @GetMapping("/{fileId}")
    public ResponseEntity<?> getFile(@PathVariable Long fileId) {
       return ResponseEntity.ok(signedUrlService.findFileById(fileId));
    }

}
