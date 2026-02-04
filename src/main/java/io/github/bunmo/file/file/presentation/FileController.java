package io.github.bunmo.file.file.presentation;

import io.github.bunmo.file.common.web.ApiResponse;
import io.github.bunmo.file.file.application.FileService;
import io.github.bunmo.file.file.application.FileService.ProfileImageData;
import io.github.bunmo.file.file.presentation.doc.FileControllerDoc;
import io.github.bunmo.file.file.presentation.dto.FileResultCode;
import io.github.bunmo.file.file.presentation.dto.ProfileUploadResponse;
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

@RestController
@RequestMapping("/api/v1/files")
public class FileController implements FileControllerDoc {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileUploadResponse>> uploadProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("file") MultipartFile file
    ) {
        String uuid = userDetails.getUsername();
        ProfileUploadResponse response = fileService.uploadProfileImage(uuid, file);
        return ResponseEntity.ok(ApiResponse.success(FileResultCode.PROFILE_UPLOADED, response));
    }

    @Override
    @GetMapping("/{uuid}/profile/{timestamp}")
    public ResponseEntity<InputStreamResource> getProfile(
            @PathVariable String uuid,
            @PathVariable String timestamp,
            @RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch
    ) {
        ProfileImageData imageData = fileService.getProfileImage(uuid, Long.parseLong(timestamp));

        if (ifNoneMatch != null && ifNoneMatch.equals("\"" + imageData.etag() + "\"")) {
            return ResponseEntity.status(304).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageData.contentType()))
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
                .eTag(imageData.etag())
                .body(new InputStreamResource(imageData.inputStream()));
    }
}
