package roomescape.domain.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.model.Member;
import roomescape.global.annotation.LoginMember;
import roomescape.global.utils.Cookies;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest,
        HttpServletResponse httpServletResponse) {
        String token = loginService.requestLogin(loginRequest);
        httpServletResponse.addCookie(Cookies.generate(token));
    }

    @GetMapping("/login/check")
    public LoginResponseDto checkLogin(@LoginMember Member member) {
        return new LoginResponseDto(member.getName());
    }

    @PostMapping("/logout")
    public void logout(@LoginMember Member member, HttpServletResponse httpServletResponse) {
        Cookie cookie = Cookies.generate("", 0);
        httpServletResponse.addCookie(cookie);
    }
}
