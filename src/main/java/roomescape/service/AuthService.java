package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.common.exception.AuthorizationException;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.impl.JdbcMemberRepository;

@Service
public class AuthService {
    private final JdbcMemberRepository jdbcMemberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider, JdbcMemberRepository jdbcMemberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jdbcMemberRepository = jdbcMemberRepository;
    }

    public String tokenLogin(LoginRequest request) {
        Member member = jdbcMemberRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthorizationException("인증되지 않은 유저 정보입니다."));
        if (checkInvalidLogin(member, request)) {
            throw new AuthorizationException("인증되지 않은 유저 정보입니다.");
        }
        return jwtTokenProvider.createToken(member);
    }

    public boolean checkInvalidLogin(Member member, LoginRequest request) {
        return member.checkInvalidLogin(request.email(), request.password());
    }
}
