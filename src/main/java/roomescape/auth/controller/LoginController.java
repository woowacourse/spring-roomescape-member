package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.MemberProfileResponse;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.service.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {

    public static final String TOKEN_COOKIE_NAME = "token";
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public void login(HttpServletResponse response, @RequestBody TokenRequest request) {
        String token = authService.createToken(request);
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/check")
    public MemberProfileResponse checkMember(@LoginMemberId Long memberId) {
        return memberService.findMemberProfile(memberId);
    }

}
