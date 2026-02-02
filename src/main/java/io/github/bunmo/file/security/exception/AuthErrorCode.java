package io.github.bunmo.file.security.exception;

import io.github.bunmo.file.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements ErrorCode {
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_001", "유효하지 않은 토큰입니다"),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_002", "만료된 토큰입니다"),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_003", "지원하지 않는 토큰입니다"),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_004", "접근이 거부되었습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED_005", "인증이 필요합니다"),
    ;

    private final HttpStatus statusCode;
    private final String code;
    private final String message;

    AuthErrorCode(HttpStatus statusCode, String code, String message) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus statusCode() {
        return this.statusCode;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
