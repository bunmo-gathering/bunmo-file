package io.github.bunmo.file.file.presentation.doc;

import io.github.bunmo.file.common.web.ApiResponse;
import io.github.bunmo.file.file.presentation.dto.ProfileUploadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File", description = "파일 관리 API")
public interface FileControllerDoc {
    @Operation(summary = "프로필 이미지 업로드", description = "인증된 사용자의 프로필 이미지를 업로드합니다")
    ResponseEntity<ApiResponse<ProfileUploadResponse>> uploadProfile(
            @Parameter(hidden = true)
            UserDetails userDetails,
            @Parameter(description = "프로필 이미지 파일")
            MultipartFile file
    );

    @Operation(summary = "프로필 이미지 조회", description = "UUID로 프로필 이미지를 조회합니다 (인증 불필요)")
    ResponseEntity<InputStreamResource> getProfile(
            @Parameter(description = "회원 UUID")
            @PathVariable String uuid,
            @Parameter(description = "캐시 버스팅용 타임스탬프")
            @PathVariable String timestamp,
            String ifNoneMatch
    );
}
