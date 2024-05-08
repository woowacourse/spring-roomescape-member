package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        Member member = loginService.findMember(loginRequest);
        TokenResponse tokenResponse = loginService.createToken(member);
        Cookie cookie = loginService.generateCookie(tokenResponse);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
