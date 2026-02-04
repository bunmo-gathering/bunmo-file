package io.github.bunmo.file.common.web;

import io.github.bunmo.file.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    private final Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleCustomException(BusinessException e) {
        log.debug(e.getMessage());
        return ResponseEntity
                .status(e.errorCode().statusCode())
                .body(ApiResponse.error(e.errorCode()));
    }
}
