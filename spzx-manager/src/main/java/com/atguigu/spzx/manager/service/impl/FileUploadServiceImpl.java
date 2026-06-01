package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.manager.properties.MinioProperties;
import com.atguigu.spzx.manager.service.FileUploadService;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String fileUpload(MultipartFile file) {
        try {
            // 创建MinioClient
            MinioClient minioClient = MinioClient.builder()
                            .endpoint(minioProperties.getEndpointUrl())
                            .credentials(minioProperties.getAccessKey(), minioProperties.getSecreKey())
                            .build();

            // 创建bucket
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            } else {
                System.out.println("Bucket 'spzx-bucket' already exists.");
            }

            // 设置存储对象名称
            String uuid = UUID.randomUUID().toString().replace("-","");
            String fileName =  uuid + file.getOriginalFilename();
            // 443e1e772bef482c95be28704bec58a901.jpg
            System.out.println(fileName);

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucketName())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .object(fileName)
                    .build());
            return minioProperties.getEndpointUrl() + "/" + minioProperties.getBucketName() + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
    }
}
