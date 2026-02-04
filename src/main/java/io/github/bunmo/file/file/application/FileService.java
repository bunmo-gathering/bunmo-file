package io.github.bunmo.file.file.application;

import io.github.bunmo.file.file.exception.FileErrorCode;
import io.github.bunmo.file.file.exception.FileException;
import io.github.bunmo.file.file.infrastructure.storage.StorageClient;
import io.github.bunmo.file.file.presentation.dto.ProfileUploadResponse;
import io.github.bunmo.file.member.domain.Member;
import io.github.bunmo.file.member.infrastructure.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Set;

@Service
public class FileService {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    private final StorageClient storageClient;
    private final MemberRepository memberRepository;

    public FileService(StorageClient storageClient, MemberRepository memberRepository) {
        this.storageClient = storageClient;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ProfileUploadResponse uploadProfileImage(String uuid, MultipartFile file) {
        validateFile(file);

        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new FileException(FileErrorCode.MEMBER_NOT_FOUND));

        String objectName = buildProfileObjectName(uuid);

        if (member.getProfileImageUrl() != null) {
            storageClient.delete(objectName);
        }

        try (InputStream inputStream = file.getInputStream()) {
            storageClient.upload(objectName, inputStream, file.getSize(), file.getContentType());
        } catch (FileException e) {
            throw e;
        } catch (Exception e) {
            throw new FileException(FileErrorCode.FILE_UPLOAD_FAILED);
        }

        String profileUrl = "/api/v1/files/profile/" + uuid;
        member.updateProfileImageUrl(profileUrl);

        return ProfileUploadResponse.of(uuid);
    }

    @Transactional(readOnly = true)
    public ProfileImageData getProfileImage(String uuid) {
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new FileException(FileErrorCode.MEMBER_NOT_FOUND));

        if (member.getProfileImageUrl() == null) {
            throw new FileException(FileErrorCode.FILE_NOT_FOUND);
        }

        String objectName = buildProfileObjectName(uuid);
        InputStream inputStream = storageClient.download(objectName);
        String etag = storageClient.getETag(objectName);
        String contentType = storageClient.getContentType(objectName);

        return new ProfileImageData(inputStream, contentType, etag);
    }

    @Transactional
    public void deleteProfileImage(String uuid) {
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new FileException(FileErrorCode.MEMBER_NOT_FOUND));

        if (member.getProfileImageUrl() == null) {
            throw new FileException(FileErrorCode.FILE_NOT_FOUND);
        }

        String objectName = buildProfileObjectName(uuid);
        storageClient.delete(objectName);
        member.updateProfileImageUrl(null);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileException(FileErrorCode.FILE_NOT_FOUND);
        }

        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new FileException(FileErrorCode.INVALID_FILE_TYPE);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileException(FileErrorCode.FILE_SIZE_EXCEEDED);
        }
    }

    private String buildProfileObjectName(String uuid) {
        return "profiles/" + uuid;
    }

    public record ProfileImageData(InputStream inputStream, String contentType, String etag) {}
}
