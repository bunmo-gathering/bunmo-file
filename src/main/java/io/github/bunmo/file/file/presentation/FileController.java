package io.github.bunmo.file.file.presentation;

import io.github.bunmo.file.common.web.ApiResponse;
import io.github.bunmo.file.file.application.FileService;
import io.github.bunmo.file.file.application.FileService.ProfileImageData;
import io.github.bunmo.file.file.presentation.dto.FileResultCode;
import io.github.bunmo.file.file.presentation.dto.ProfileUploadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Tag(name = "File", description = "파일 관리 API")
@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "프로필 이미지 업로드", description = "인증된 사용자의 프로필 이미지를 업로드합니다")
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileUploadResponse>> uploadProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "프로필 이미지 파일")
            @RequestParam("file") MultipartFile file
    ) {
        String uuid = userDetails.getUsername();
        ProfileUploadResponse response = fileService.uploadProfileImage(uuid, file);
        return ResponseEntity.ok(ApiResponse.success(FileResultCode.PROFILE_UPLOADED, response));
    }

    @Operation(summary = "프로필 이미지 조회", description = "UUID로 프로필 이미지를 조회합니다 (인증 불필요)")
    @GetMapping("/profile/{uuid}")
    public ResponseEntity<InputStreamResource> getProfile(
            @Parameter(description = "회원 UUID")
            @PathVariable String uuid,
            @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch
    ) {
        ProfileImageData imageData = fileService.getProfileImage(uuid);

        if (ifNoneMatch != null && ifNoneMatch.equals("\"" + imageData.etag() + "\"")) {
            return ResponseEntity.status(304).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageData.contentType()))
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
                .eTag(imageData.etag())
                .body(new InputStreamResource(imageData.inputStream()));
    }

    @Operation(summary = "프로필 이미지 삭제", description = "인증된 사용자의 프로필 이미지를 삭제합니다")
    @DeleteMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> deleteProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String uuid = userDetails.getUsername();
        fileService.deleteProfileImage(uuid);
        return ResponseEntity.ok(ApiResponse.success(FileResultCode.PROFILE_DELETED, null));
    }
}
