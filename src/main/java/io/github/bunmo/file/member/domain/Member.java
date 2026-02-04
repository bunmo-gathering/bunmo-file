package io.github.bunmo.file.member.domain;

import io.github.bunmo.file.member.domain.enums.ActiveType;
import io.github.bunmo.file.member.domain.enums.RoleType;
import io.github.bunmo.file.member.domain.vo.MemberProfile;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String uuid;

    @Embedded
    private MemberProfile memberProfile;

    @Enumerated(EnumType.STRING)
    @Column(name = "active_type", nullable = false)
    private ActiveType activeType;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    protected Member() { }

    public String getUuid() {
        return uuid;
    }

    public RoleType role() {
        return role;
    }

    public String getProfileImageUrl() {
        return memberProfile != null ? memberProfile.profileImageUrl() : null;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.memberProfile = new MemberProfile(profileImageUrl);
    }
}
