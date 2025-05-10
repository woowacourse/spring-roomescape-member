package roomescape.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.AccessToken;
import roomescape.business.service.member.MemberService;

/**
 * 쿠키에서 AccessToken을 추출하여 로그인한 회원 정보를 요청에 담는 인터셉터입니다.
 */
public final class AuthenticationInterceptor implements HandlerInterceptor {

    public static final String LOGIN_MEMBER_ATTRIBUTE_NAME = "loginMember";
    private static final String ACCESS_TOKEN_COOKIE_NAME = "token";
    private final MemberService memberService;

    public AuthenticationInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        try {
            AccessToken accessToken = extractAccessToken(request);
            LoginMember loginMember = memberService.getMemberFromToken(accessToken);
            request.setAttribute(LOGIN_MEMBER_ATTRIBUTE_NAME, loginMember);
        } catch (Exception ignored) {
            request.setAttribute(LOGIN_MEMBER_ATTRIBUTE_NAME, LoginMember.ANONYMOUS);
        }
        return true;
    }

    private AccessToken extractAccessToken(HttpServletRequest request) {
        String token = getAccessTokenFromCookies(request.getCookies());
        if (token == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        return AccessToken.of(token);
    }

    private String getAccessTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> ACCESS_TOKEN_COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
