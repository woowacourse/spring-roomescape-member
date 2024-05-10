package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.domain.Role;
import roomescape.dto.response.AuthResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.exception.InvalidAccessException;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CheckAdminInterceptor implements HandlerInterceptor {

    private final AuthService authService;
    private final MemberService memberService;

    public CheckAdminInterceptor(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new InvalidAccessException("관리자 페이지에 접근할 권한이 없습니다.");
        }
        Cookie findCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .orElseThrow(() -> new InvalidAccessException("관리자 페이지에 접근할 권한이 없습니다."));

        validateAdmin(findCookie);
        return true;
    }

    private void validateAdmin(Cookie findCookie) {
        AuthResponse authResponse = authService.findPayloadByToken(findCookie.getValue());
        Optional<MemberResponse> optionalMemberResponse = memberService.findMemberByEmail(authResponse.email());
        if (optionalMemberResponse.isEmpty() || optionalMemberResponse.get().role() != Role.ADMIN) {
            throw new InvalidAccessException("관리자 페이지에 접근할 권한이 없습니다.");
        }
    }
}
