package roomescape.domain.login.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.login.JwtTokenProvider;
import roomescape.domain.login.domain.Member;
import roomescape.domain.login.dto.LoginRequest;
import roomescape.domain.login.service.LoginService;

@RestController
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;
    private final LoginService loginService;

    public LoginController(JwtTokenProvider jwtTokenProvider, LoginService loginService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Member member = loginService.findMemberByEmailAndPassword(loginRequest.email(),
                loginRequest.password());
        Cookie cookie = new Cookie("token", jwtTokenProvider.generateToken(member.getName()));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
