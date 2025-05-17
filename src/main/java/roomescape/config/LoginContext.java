package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import roomescape.business.domain.member.Member;
import roomescape.exception.MemberException;
import roomescape.persistence.MemberRepository;

@Component
public class LoginContext {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "token";

    private final MemberRepository memberRepository;

    public LoginContext(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginMember getLoginMember(HttpServletRequest request) {
        String token = extractTokenFrom(request.getCookies());
        if (token == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        AccessToken accessToken = AccessToken.of(token);
        return getMemberFromToken(accessToken);
    }

    public AccessToken getAccessToken(HttpServletRequest request) {
        String token = extractTokenFrom(request.getCookies());
        if (token == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return AccessToken.of(token);
    }

    private String extractTokenFrom(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private LoginMember getMemberFromToken(AccessToken accessToken) {
        Long memberIdFromToken = accessToken.extractMemberId();
        Member member = memberRepository.findById(memberIdFromToken)
                .orElseThrow(() -> new MemberException("사용자가 존재하지 않습니다."));
        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getRole()
        );
    }
}
