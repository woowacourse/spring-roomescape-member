package roomescape.auth.service;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import roomescape.auth.jwt.AuthTokenProvider;
import roomescape.auth.service.dto.CreateTokenServiceRequest;
import roomescape.exception.custom.AuthorizationException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final AuthTokenProvider authTokenProvider;

    public AuthService(final MemberDao memberDao, final AuthTokenProvider authTokenProvider) {
        this.memberDao = memberDao;
        this.authTokenProvider = authTokenProvider;
    }

    public String createToken(final CreateTokenServiceRequest request) {
        final Member member = findMemberByEmail(request.email());
        member.validateRightPassword(request.password());

        return authTokenProvider.createToken(member);
    }

    private Member findMemberByEmail(final String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("존재하지 않는 이메일 입니다"));
    }

    public ResponseCookie createCookie(final String accessToken) {
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

    public Member findMemberByToken(final String token) {
        validateTokenExisted(token);
        final long memberId = Long.parseLong(authTokenProvider.extractPayload(token));

        return memberDao.findById(memberId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 멤버 입니다"));
    }

    private void validateTokenExisted(final String token) {
        if (token == null || token.isBlank()) {
            throw new AuthorizationException("로그인 토큰이 존재하지 않습니다");
        }
    }

    public boolean isAdmin(final String token) {
        return authTokenProvider.extractRole(token).equals("Admin");
    }
}
