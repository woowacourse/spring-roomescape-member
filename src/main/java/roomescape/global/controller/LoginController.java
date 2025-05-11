package roomescape.global.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public CheckResponseDto checkLogin(HttpServletRequest request) {
        String token = Cookies.get(request.getCookies());
        return authService.authenticateByToken(token);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String token = Cookies.get(request.getCookies());
        Cookie cookie = Cookies.generate(token, 0);
        httpServletResponse.addCookie(cookie);
    }
}
