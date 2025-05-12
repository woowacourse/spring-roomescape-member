package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.controller.annotation.LoginMemberId;
import roomescape.auth.controller.annotation.LoginRequired;
import roomescape.auth.dto.MemberProfileResponse;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.service.AuthService;
import roomescape.member.service.MemberService;

@RestController
public class LoginController {

    public static final String TOKEN_COOKIE_NAME = "token";
    private final AuthService authService;
    private final MemberService memberService;

    public LoginController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public void login(HttpServletResponse response, @Valid @RequestBody TokenRequest request) {
        String token = authService.createToken(request);
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @LoginRequired
    @GetMapping("/login/check")
    public MemberProfileResponse checkMember(@LoginMemberId Long memberId) {
        return memberService.findMemberProfile(memberId);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
