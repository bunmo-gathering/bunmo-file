package io.github.bunmo.file.file.infrastructure.storage;

import java.io.InputStream;

public interface StorageClient {

    void upload(String objectName, InputStream inputStream, long size, String contentType);

    InputStream download(String objectName);

    void delete(String objectName);

    boolean exists(String objectName);

    String getETag(String objectName);

    String getContentType(String objectName);
}
