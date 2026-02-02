package io.github.bunmo.file.common.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.bunmo.file.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"code", "message", "data", "errors"})
public class ApiResponse<T> {
    @Schema(description = "결과 코드", example = "OAUTH_001")
    private final String code;
    @Schema(description = "결과 메시지", example = "카카오 사용자 정보 요청에 실패했습니다")
    private final String message;
    @Schema(description = "요청에 대한 응답 데이터", example = "{\"id\":\"1\"}")
    private final T data;
    private final List<FieldError> errors;

    private ApiResponse(String code, String message, T data, List<FieldError> errors) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public record FieldError(String field, String message) {}

    public static <T> ApiResponse<T> success(ResultCode resultCode, T data) {
        return new ApiResponse<>(resultCode.code(), resultCode.message(), data, null);
    }

    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return new ApiResponse(errorCode.code(), errorCode.message(), null, null);
    }
}
