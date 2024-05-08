package roomescape.controller.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.auth.CookieGenerator;
import roomescape.auth.Token;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.serviceimpl.LoginService;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final CookieGenerator cookieGenerator;
    private final LoginService loginService;

    public LoginController(
            final CookieGenerator cookieGenerator,
            final LoginService loginService
    ) {
        this.cookieGenerator = cookieGenerator;
        this.loginService = loginService;
    }

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        Token token = loginService.login(request.email(), request.password());
        Cookie cookie = cookieGenerator.generate(token);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Token token = cookieGenerator.getToken(request.getCookies());
        MemberResponse memberResponse = loginService.checkMember(token);
        return ResponseEntity.ok(memberResponse);
    }
}
