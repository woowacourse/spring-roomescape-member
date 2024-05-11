package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberResponse;
import roomescape.infrastructure.AuthorizationExtractor;
import roomescape.service.AuthService;

@RestController
public class LoginController {

    private final AuthService authService;

    private final AuthorizationExtractor authorizationExtractor;

    public LoginController(AuthService authService, AuthorizationExtractor authorizationExtractor) {
        this.authService = authService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest,
                                      HttpServletResponse response) {
        Cookie cookie = new Cookie("token", authService.createToken(loginRequest));
        response.addCookie(cookie);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> findMemberInformation(HttpServletRequest request) {
        String token = authorizationExtractor.extract(request);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authService.findMemberByToken(token));
    }
}
