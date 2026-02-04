package io.github.bunmo.file.member.domain.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public record MemberProfile(
        String profileImageUrl
) {
}
