package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberPassword;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.exception.AuthorizationException;
import roomescape.jwt.JwtTokenProvider;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public LoginResponse createToken(LoginRequest request) {
        String accessToken = jwtTokenProvider.createToken(request.getEmail());
        return new LoginResponse(accessToken);
    }

    public Member findAuthInfo(String token) {
        validateNotExpiredToken(token);
        String payload = jwtTokenProvider.getPayload(token);
        MemberEmail email = new MemberEmail(payload);
        return findByEmail(email);
    }

    public void checkLoginInfo(LoginRequest request) {
        MemberEmail email = new MemberEmail(request.getEmail());
        MemberPassword password = new MemberPassword(request.getPassword());
        if (!memberDao.existByEmailAndMemberPassword(email, password)) {
            throw new AuthorizationException("아이디 또는 비밀번호 오류입니다.");
        }
    }

    private void validateNotExpiredToken(String token) {
        if (!jwtTokenProvider.checkNotExpired(token)) {
            throw new AuthorizationException("기한이 만료된 토큰입니다.");
        }
    }

    private Member findByEmail(MemberEmail email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("아이디 또는 비밀번호 오류입니다."));
    }
}
