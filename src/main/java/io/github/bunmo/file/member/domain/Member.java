package io.github.bunmo.file.member.domain;

import io.github.bunmo.file.member.domain.enums.ActiveType;
import io.github.bunmo.file.member.domain.enums.RoleType;
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
}
