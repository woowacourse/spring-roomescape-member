package roomescape.user.ui;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.application.UserService;
import roomescape.user.dto.LoginRequest;
import roomescape.user.application.AuthService;
import roomescape.user.dto.LoginResponse;

@RestController
public class UserApiController {

    private final AuthService authService;
    private final UserService userService;

    public UserApiController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> findLoginUser(@CookieValue(name = "token") String token) {
        String userId = authService.getPayload(token);
        String name = userService.findNameById(Long.parseLong(userId));
        return ResponseEntity.ok()
                .body(new LoginResponse(name));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> createToken(@RequestBody LoginRequest loginRequest) {
        Long userId = userService.findIdByEmailAndPassword(loginRequest);
        String accessToken = authService.createToken(String.valueOf(userId));
        ResponseCookie cookie = ResponseCookie.from("token", accessToken)
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
