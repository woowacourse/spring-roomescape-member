package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberLoginCheckResponse;
import roomescape.service.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public LoginController(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @PostMapping
    public ResponseEntity<Void> login(HttpServletResponse response, @RequestBody @Valid LoginRequest request) {
        String token = authService.tokenLogin(request);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/check")
    public ResponseEntity<MemberLoginCheckResponse> loginCheck(HttpServletRequest request) {
        String token = authorizationExtractor.extractTokenFromCookie(request);
        MemberLoginCheckResponse response = authService.findMemberByToken(token);
        return ResponseEntity.ok(response);
    }

}
