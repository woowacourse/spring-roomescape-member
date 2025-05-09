package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.service.MemberAuthService;
import roomescape.auth.service.dto.request.LoginRequest;
import roomescape.auth.service.dto.response.CheckResponse;
import roomescape.auth.service.dto.response.LoginResponse;
import roomescape.config.AuthenticationPrincipal;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final MemberAuthService service;

    public LoginController(MemberAuthService service) {
        this.service = service;
    }

    @PostMapping
    public void login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = service.login(request);
        Cookie cookie = new Cookie("token", loginResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/check")
    public ResponseEntity<CheckResponse> checkLogin(@AuthenticationPrincipal String token) {
        CheckResponse response = service.checkLogin(token);
        return ResponseEntity.ok(response);
    }
}
