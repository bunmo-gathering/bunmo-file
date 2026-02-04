package io.github.bunmo.file.file.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 이미지 업로드 응답")
public record ProfileUploadResponse(
        @Schema(description = "회원 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
        String uuid,

        @Schema(description = "프로필 이미지 URL", example = "/api/v1/files/profile/550e8400-e29b-41d4-a716-446655440000")
        String profileUrl
) {
    public static ProfileUploadResponse of(String uuid) {
        return new ProfileUploadResponse(uuid, "/api/v1/files/profile/" + uuid);
    }
}
