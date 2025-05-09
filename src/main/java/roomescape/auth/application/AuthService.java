package roomescape.auth.application;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.dto.request.CreateTokenServiceRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.infrastructure.MemberDao;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(final MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(CreateTokenServiceRequest request) {
        LoginMember member = findMemberByEmail(request.email());
        member.validateRightPassword(request.password());

        return jwtTokenProvider.createToken(member);
    }

    private LoginMember findMemberByEmail(String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("존재하지 않는 이메일 입니다"));
    }

    public ResponseCookie createCookie(String accessToken) {
        return ResponseCookie.from("token", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(60 * 60)
                .build();
    }
}
