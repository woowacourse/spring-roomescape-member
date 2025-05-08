package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public void login(
            @RequestBody LoginRequest request, HttpServletResponse response) {
        Cookie loginCookie = loginService.doLogin(request);
        response.addCookie(loginCookie);
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> checkMember(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        LoginCheckResponse loginCheckResponse = new LoginCheckResponse(loginService.getNameFromCookie(cookies));
        return ResponseEntity.ok().body(loginCheckResponse);
    }
}
