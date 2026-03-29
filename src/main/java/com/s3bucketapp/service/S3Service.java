package com.s3bucketapp.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
public class S3Service {

    @Autowired private S3Client s3Client;
    @Value("${aws.s3.bucket-name}") private String bucketName;

    // 1. Upload
    public String uploadFile(String fileName, MultipartFile file) throws IOException {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName).key(fileName).contentType(file.getContentType()).build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
        return "File uploaded: " + fileName;
    }

    // 2. Download (Get as bytes)
    public byte[] downloadFile(String fileName) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName).key(fileName).build();

        ResponseInputStream<GetObjectResponse> res = s3Client.getObject(getObjectRequest);

        return res.readAllBytes();
    }

    // 3. Delete
    public String deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName).key(fileName).build();
        s3Client.deleteObject(deleteObjectRequest);
        return "Deleted: " + fileName;

    }
    // 4. List All Files
    public List<String> listAllFiles() {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder().bucket(bucketName).build();
        return s3Client.listObjectsV2(listRequest).contents().stream()
                .map(S3Object::key).collect(Collectors.toList());
    }

}
