package io.github.bunmo.file.file.presentation.dto;

import io.github.bunmo.file.common.web.ResultCode;
import org.springframework.http.HttpStatus;

public enum FileResultCode implements ResultCode {
    PROFILE_UPLOADED(HttpStatus.OK, "FILE_001", "프로필 이미지가 업로드되었습니다"),
    PROFILE_DELETED(HttpStatus.OK, "FILE_002", "프로필 이미지가 삭제되었습니다"),
    ;

    private final HttpStatus statusCode;
    private final String code;
    private final String message;

    FileResultCode(HttpStatus statusCode, String code, String message) {
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
