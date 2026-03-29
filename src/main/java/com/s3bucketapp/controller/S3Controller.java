package com.s3bucketapp.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.s3bucketapp.dto.FileUploadRequest;
import com.s3bucketapp.service.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;

@RestController
@RequestMapping("/api/s3")
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file to S3")
    public String upload(
            @Parameter(description = "Select file to upload", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestParam("file") MultipartFile file)
            throws IOException {

        return s3Service.uploadFile(file.getOriginalFilename(), file);
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<byte[]> viewFile(@PathVariable String fileName) throws IOException {
        byte[] data = s3Service.downloadFile(fileName);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(data);
    }

    @GetMapping("/all")
    public List<String> listFiles() {
        return s3Service.listAllFiles();
    }

    @DeleteMapping("/delete/{fileName}")
    public String delete(@PathVariable String fileName) {
        return s3Service.deleteFile(fileName);
    }

    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public String uploadMultiple(@ModelAttribute FileUploadRequest request) throws IOException {
    MultipartFile[] files = request.getFiles();
    for (MultipartFile file : files) {
        s3Service.uploadFile(file.getOriginalFilename(), file);
    }
    return "Upload started for " + files.length + " files.";
}
}
