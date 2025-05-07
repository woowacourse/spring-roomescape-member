package roomescape.presentation.api;

import jakarta.validation.Valid;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthRequired;
import roomescape.business.model.entity.User;
import roomescape.business.model.vo.AuthToken;
import roomescape.business.model.vo.LoginInfo;
import roomescape.business.service.AuthService;
import roomescape.business.service.UserService;
import roomescape.presentation.dto.request.LoginRequest;
import roomescape.presentation.dto.response.UserResponse;

@RestController
public class AuthApiController {

    private final AuthService authService;
    private final UserService userService;

    public AuthApiController(final AuthService authService, final UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {
        AuthToken authToken = authService.authenticate(request.email(), request.password());

        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = ResponseCookie.from("authToken", authToken.value())
                .httpOnly(true)
                .sameSite(Cookie.SameSite.STRICT.name())
                .build();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().headers(headers).build();
    }

    @GetMapping("/login/check")
    @AuthRequired
    public ResponseEntity<UserResponse> check(LoginInfo loginInfo) {
        User user = userService.getById(loginInfo.id());
        return ResponseEntity.ok(UserResponse.from(user));
    }
}
