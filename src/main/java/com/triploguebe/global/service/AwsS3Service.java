package com.triploguebe.global.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile file, String path, String fileName){
        try {
            String newFileName = getNewFileName(file, fileName);

            InputStream fileInputStream = file.getInputStream();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(bucketName, path + newFileName, fileInputStream, metadata);
            fileInputStream.close();

            return newFileName;

        }catch (IOException e){
            throw new IllegalArgumentException("S3 파일 업로드에 실패하였습니다.");
        }
    }

    private static String getNewFileName(MultipartFile file, String fileName) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 비어있습니다.");
        }

        String fileExtension = originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1)
                : "";

        if (fileExtension.isEmpty()) {
            throw new IllegalArgumentException("파일 확장자가 올바르지 않습니다.");
        }

        return fileName + "." + fileExtension;
    }

    public void deleteFile(String fullPath) {
        amazonS3.deleteObject(bucketName, fullPath);
    }
}
