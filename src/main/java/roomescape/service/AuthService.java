package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.dao.MemberDAO;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.TokenResponse;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDAO memberDAO;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberDAO memberDAO) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDAO = memberDAO;
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        if (!memberDAO.existMember(loginRequest.email(), loginRequest.password())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못 입력되었습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(loginRequest.email());
        return new TokenResponse(accessToken);
    }

    public Member findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public Member findMember(String principal) {
        return memberDAO.findByEmail(principal);
    }
}
