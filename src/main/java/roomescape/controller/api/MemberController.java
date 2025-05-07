package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.request.MemberLoginRequest;
import roomescape.controller.dto.request.MemberSignUpRequest;
import roomescape.controller.dto.request.MemberSignUpResponse;
import roomescape.controller.dto.response.MemberLoginCheckResponse;
import roomescape.service.MemberLoginService;
import roomescape.service.dto.request.MemberLoginCreation;
import roomescape.service.dto.request.MemberSignUpCreation;
import roomescape.service.dto.response.MemberLoginCheckResult;
import roomescape.service.dto.response.MemberSignUpResult;

@RestController
public class MemberController {

    private final MemberLoginService memberLoginService;

    public MemberController(final MemberLoginService memberLoginService) {
        this.memberLoginService = memberLoginService;
    }

    @PostMapping("/members")
    public MemberSignUpResponse signup(@RequestBody @Valid MemberSignUpRequest request) {
        MemberSignUpCreation creation = MemberSignUpCreation.from(request);
        MemberSignUpResult signUpResult = memberLoginService.signup(creation);
        return MemberSignUpResponse.from(signUpResult);
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid final MemberLoginRequest request,
                      final HttpServletResponse servletResponse) {
        String loginToken = memberLoginService.login(MemberLoginCreation.from(request));
        addCookieToken(servletResponse, loginToken);
    }

    private void addCookieToken(final HttpServletResponse servletResponse, final String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(1000 * 60 * 60 * 24);
        servletResponse.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberLoginCheckResponse> checkLogin(HttpServletRequest request) {
        String tokenCookie = getTokenCookie(request);
        MemberLoginCheckResult memberLoginCheckResult = memberLoginService.varifyToken(tokenCookie);
        return ResponseEntity.ok(MemberLoginCheckResponse.from(memberLoginCheckResult));
    }

    private String getTokenCookie(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return "";
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
