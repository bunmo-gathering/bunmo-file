package io.github.bunmo.file.common.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus statusCode();
    String code();
    String message();
}
