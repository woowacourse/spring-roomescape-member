package roomescape.controller;

import static roomescape.controller.TokenExtractor.extractTokenFromCookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.exception.AuthorizationException;
import roomescape.service.MemberAuthService;
import roomescape.service.response.MemberAppResponse;

@Component
public class CheckLoginInterceptor implements HandlerInterceptor {

    public static final String ADMIN = "ADMIN";
    private final MemberAuthService memberAuthService;

    public CheckLoginInterceptor(final MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getCookies() == null) {
            throw new IllegalArgumentException("쿠키가 없습니다. 다시 로그인 해주세요.");
        }
        String token = extractTokenFromCookie(request.getCookies());
        MemberAppResponse appResponse = memberAuthService.findMemberByToken(token);
        if (token == null || !appResponse.role().equals(ADMIN)) {
            throw new AuthorizationException("접근 권한이 없습니다.");
        }

        return true;
    }
}
