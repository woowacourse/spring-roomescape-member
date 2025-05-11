package roomescape.global.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.model.Member;
import roomescape.global.annotation.LoginMember;
import roomescape.global.auth.AuthService;
import roomescape.global.auth.Cookies;
import roomescape.global.dto.CheckResponseDto;
import roomescape.global.dto.TokenRequest;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void login(@RequestBody TokenRequest tokenRequest,
        HttpServletResponse httpServletResponse) {
        String token = authService.requestLogin(tokenRequest);
        httpServletResponse.addCookie(Cookies.generate(token));
    }

    @GetMapping("/login/check")
    public CheckResponseDto checkLogin(@LoginMember Member member) {
        return new CheckResponseDto(member.getName());
    }

    @PostMapping("/logout")
    public void logout(@LoginMember Member member, HttpServletResponse httpServletResponse) {
        Cookie cookie = Cookies.generate("", 0);
        httpServletResponse.addCookie(cookie);
    }
}
