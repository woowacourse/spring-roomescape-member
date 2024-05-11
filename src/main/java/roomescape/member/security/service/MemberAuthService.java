package roomescape.member.security.service;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Service;
import roomescape.exception.AuthorizationMismatchException;
import roomescape.exception.IllegalAuthorizationException;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.security.crypto.PasswordEncoder;
import roomescape.member.security.crypto.TokenProvider;

@Service
public class MemberAuthService {

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public MemberAuthService(PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public void validateAuthentication(Member member,
            MemberLoginRequest memberLoginRequest) throws AuthorizationMismatchException {
        if (!passwordEncoder.matches(memberLoginRequest.password(), member.getPassword())) {
            throw new AuthorizationMismatchException("비밀번호가 일치하지 않습니다.");
        }
    }

    public String publishToken(Member member) {
        Date now = new Date();
        return tokenProvider.createToken(member, now);
    }

    public Map<String, String> extractPayload(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        return tokenProvider.getPayload(token);
    }

    public String extractNameFromPayload(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        Map<String, String> tokenPayload = tokenProvider.getPayload(token);
        return tokenPayload.get("name");
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalAuthorizationException("잘못된 쿠키 값입니다."));
    }

}
