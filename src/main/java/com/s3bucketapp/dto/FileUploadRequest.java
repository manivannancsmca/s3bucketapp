package com.s3bucketapp.dto;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {
    private MultipartFile[] files;

    public MultipartFile[] getFiles() { return files; }
    public void setFiles(MultipartFile[] files) { this.files = files; }
}
