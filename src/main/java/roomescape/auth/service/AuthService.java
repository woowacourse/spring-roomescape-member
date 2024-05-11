package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.MemberInfo;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        if (!memberDao.isMemberExist(loginRequest.email(), loginRequest.password())) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        String accessToken = jwtTokenProvider.createToken(loginRequest.email());
        return new TokenResponse(accessToken);
    }

    public MemberInfo findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    private MemberInfo findMember(String principal) {
        return memberDao.findByEmail(principal)
                .orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
    }
}
