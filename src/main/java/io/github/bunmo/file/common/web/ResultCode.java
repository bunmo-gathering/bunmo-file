package io.github.bunmo.file.common.web;

import org.springframework.http.HttpStatus;

public interface ResultCode {
    HttpStatus statusCode();
    String code();
    String message();
}
