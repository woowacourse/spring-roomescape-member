package roomescape.config;

import static roomescape.config.LoginMemberArgumentResolver.ACCESS_TOKEN_COOKIE_NAME;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.AccessToken;
import roomescape.business.service.member.MemberService;

public class CheckAdminInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    public CheckAdminInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        try {
            AccessToken accessToken = getAccessToken(request);
            if (!memberService.getMemberFromToken(accessToken).isAdmin()) {
                writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
                return false;
            }
            return true;
        } catch (IllegalArgumentException e) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return false;
        } catch (Exception e) {
            writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
            return false;
        }
    }

    private AccessToken getAccessToken(HttpServletRequest request) {
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

    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }
}
