package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.TokenResponse;
import roomescape.auth.infra.JwtProvider;
import roomescape.exception.ArgumentException;
import roomescape.member.Member;
import roomescape.member.dao.MemberDao;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberDao jdbcMemberDao;

    public AuthService(JwtProvider jwtProvider, MemberDao jdbcMemberDao) {
        this.jwtProvider = jwtProvider;
        this.jdbcMemberDao = jdbcMemberDao;
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        String token = jwtProvider.createToken(loginRequest.email());
        return new TokenResponse(token);
    }

    public Member findMemberByToken(String token) {
        String email = jwtProvider.getEmail(token);
        return jdbcMemberDao.findMember(email)
                .orElseThrow(() -> new ArgumentException("회원 정보가 존재하지 않습니다."));
    }
}
