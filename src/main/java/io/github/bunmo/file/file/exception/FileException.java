package io.github.bunmo.file.file.exception;

import io.github.bunmo.file.common.exception.BusinessException;

public class FileException extends BusinessException {

    public FileException(FileErrorCode errorCode) {
        super(errorCode);
    }
}
