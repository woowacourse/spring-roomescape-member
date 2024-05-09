package roomescape.controller.api;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.LoginRequest;
import roomescape.exception.AuthorizationException;
import roomescape.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static final String SESSION_KEY = "USER";

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpSession session) {
        if (loginService.isInvalidLogin(request.email(), request.password())) {
            throw new AuthorizationException("이메일 혹은 패스워드가 일치하지 않습니다.");
        }
        session.setAttribute(SESSION_KEY, request.email());
        return ResponseEntity.ok().build();
    }
}
