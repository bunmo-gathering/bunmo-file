package io.github.bunmo.file.security.service;

import io.github.bunmo.file.member.domain.Member;
import io.github.bunmo.file.member.infrastructure.repository.MemberRepository;
import io.github.bunmo.file.security.CustomUserDetails;
import io.github.bunmo.file.security.exception.AuthErrorCode;
import io.github.bunmo.file.security.exception.AuthException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new AuthException(AuthErrorCode.ACCESS_DENIED));

        return CustomUserDetails.from(member);
    }
}
