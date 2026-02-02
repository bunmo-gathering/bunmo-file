package io.github.bunmo.file.member.domain.enums;

public enum ActiveType {
    PENDING, ACTIVE, INACTIVE, BANNED, WITHDRAWAL;

    public boolean isValid() {
        return this == ACTIVE || this == PENDING;
    }
}
