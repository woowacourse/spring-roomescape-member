package roomescape.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.CookieGenerator;
import roomescape.auth.Token;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.serviceimpl.LoginService;

@RestController
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

    @PostMapping
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        Token token = loginService.login(request.email(), request.password());
        response.addCookie(cookieGenerator.generate(token));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Token token = cookieGenerator.getToken(request.getCookies());
        MemberResponse memberResponse = loginService.checkMember(token);
        return ResponseEntity.ok(memberResponse);
    }
}
