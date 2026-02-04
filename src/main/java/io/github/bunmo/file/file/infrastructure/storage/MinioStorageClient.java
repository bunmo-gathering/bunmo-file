package io.github.bunmo.file.file.infrastructure.storage;

import io.github.bunmo.file.file.exception.FileErrorCode;
import io.github.bunmo.file.file.exception.FileException;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class MinioStorageClient implements StorageClient {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioStorageClient(
            MinioClient minioClient,
            @Value("${minio.bucket}") String bucket
    ) {
        this.minioClient = minioClient;
        this.bucket = bucket;
    }

    @Override
    public void upload(String objectName, InputStream inputStream, long size, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public InputStream download(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw new FileException(FileErrorCode.FILE_NOT_FOUND);
            }
            throw new FileException(FileErrorCode.FILE_DOWNLOAD_FAILED);
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FILE_DOWNLOAD_FAILED);
        }
    }

    @Override
    public void delete(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FILE_DELETE_FAILED);
        }
    }

    @Override
    public boolean exists(String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                return false;
            }
            throw new FileException(FileErrorCode.FILE_OPERATION_FAILED);
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FILE_OPERATION_FAILED);
        }
    }

    @Override
    public String getETag(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
            return stat.etag();
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw new FileException(FileErrorCode.FILE_NOT_FOUND);
            }
            throw new FileException(FileErrorCode.FILE_OPERATION_FAILED);
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FILE_OPERATION_FAILED);
        }
    }

    @Override
    public String getContentType(String objectName) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
            return stat.contentType();
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                throw new FileException(FileErrorCode.FILE_NOT_FOUND);
            }
            throw new FileException(FileErrorCode.FILE_OPERATION_FAILED);
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FILE_OPERATION_FAILED);
        }
    }
}
