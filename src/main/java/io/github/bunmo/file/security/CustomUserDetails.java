package io.github.bunmo.file.security;

import io.github.bunmo.file.member.domain.Member;
import io.github.bunmo.file.member.domain.enums.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private String uuid;
    private RoleType role;

    private CustomUserDetails(String uuid, RoleType role) {
        this.uuid = uuid;
        this.role = role;
    }

    public static CustomUserDetails from(Member member) {
        return new CustomUserDetails(member.getUuid(), member.role());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.toString()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return uuid;
    }
}
