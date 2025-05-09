package roomescape.auth.application;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.dto.request.CreateTokenServiceRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.auth.infrastructure.MemberDao;
import roomescape.exception.custom.NotExistedValueException;

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

    public String extractTokenFromCookie(final Cookie[] cookies) {
        validateCookiesExisted(cookies);

        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("");
    }

    private void validateCookiesExisted(final Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthorizationException("쿠키가 존재하지 않습니다");
        }
    }

    public LoginMember findMember(final String token) {
        validateTokenExisted(token);
        final long memberId = Long.parseLong(jwtTokenProvider.extractPayload(token));

        return memberDao.findById(memberId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 멤버 입니다"));
    }

    private void validateTokenExisted(final String token) {
        if (token == null || token.isBlank()) {
            throw new AuthorizationException("로그인 토큰이 존재하지 않습니다");
        }
    }
}
