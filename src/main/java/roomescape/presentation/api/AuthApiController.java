package roomescape.presentation.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthRequired;
import roomescape.business.model.entity.User;
import roomescape.business.model.vo.Authentication;
import roomescape.business.model.vo.Authorization;
import roomescape.business.service.AuthService;
import roomescape.business.service.UserService;
import roomescape.presentation.dto.request.LoginRequest;
import roomescape.presentation.dto.response.UserNameResponse;

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
        Authentication authentication = authService.authenticate(request.email(), request.password());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "authToken=" + authentication.token());
        return ResponseEntity.noContent().headers(headers).build();
    }

    @GetMapping("/login/check")
    @AuthRequired
    public ResponseEntity<UserNameResponse> check(Authorization authorization) {
        User user = userService.findByEmail(authorization.email());
        return ResponseEntity.ok(new UserNameResponse(user.name()));
    }
}
