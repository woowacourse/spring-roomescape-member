package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.dto.NameResponse;
import roomescape.infra.auth.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {
    AuthService authService;
    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest,
                                      HttpServletResponse response) throws BadRequestException {
        Cookie cookie = authService.login(loginRequest.email(), loginRequest.password());
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<NameResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return ResponseEntity.ok(authService.checkLogin(cookies));
        }
        log.warn("인증 정보 오류");
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
    }
}
