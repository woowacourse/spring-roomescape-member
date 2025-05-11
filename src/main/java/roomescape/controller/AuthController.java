package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.application.dto.LoginRequest;
import roomescape.application.dto.MemberResponse;
import roomescape.application.service.AuthService;
import roomescape.domain.Member;
import roomescape.global.config.AuthenticationPrincipal;

@Controller
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String displayLogin() {
        return "login";
    }

    @GetMapping("check")
    @ResponseBody
    public MemberResponse checkAuthentication(@AuthenticationPrincipal Member member) {
        return new MemberResponse(member);
    }

    @PostMapping
    @ResponseBody
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String token = authService.createTokenForAuthenticatedMember(loginRequest);

        Cookie cookie = createTokenCookie(token);
        response.addCookie(cookie);
    }

    private Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
