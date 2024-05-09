package roomescape.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.LoginCheckResponse;
import roomescape.controller.dto.LoginRequest;
import roomescape.controller.dto.TokenResponse;
import roomescape.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        String token = loginService.createToken(request.email(), request.password());

        ResponseCookie cookie = ResponseCookie.from("token", token)
            .httpOnly(true)
            .path("/")
            .build();

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> checkLogin() {
        return null; // TODO
    }
}
