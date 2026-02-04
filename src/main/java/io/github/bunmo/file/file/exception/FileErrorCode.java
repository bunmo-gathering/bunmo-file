package io.github.bunmo.file.file.exception;

import io.github.bunmo.file.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum FileErrorCode implements ErrorCode {
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE_001", "파일을 찾을 수 없습니다"),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_002", "파일 업로드에 실패했습니다"),
    FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_003", "파일 다운로드에 실패했습니다"),
    FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_004", "파일 삭제에 실패했습니다"),
    FILE_OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_005", "파일 작업에 실패했습니다"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "FILE_006", "지원하지 않는 파일 형식입니다"),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "FILE_007", "파일 크기가 제한을 초과했습니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE_008", "회원을 찾을 수 없습니다"),
    ;

    private final HttpStatus statusCode;
    private final String code;
    private final String message;

    FileErrorCode(HttpStatus statusCode, String code, String message) {
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
