package roomescape.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.auth.dto.UserCreateRequest;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.service.AuthService;

@RestController
public class UserController {

    private final AuthService authService;

    public UserController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/members")
    public ResponseEntity<UserInfoResponse> signUp(@RequestBody final UserCreateRequest userCreateRequest) {
        final UserInfoResponse register = authService.register(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(register);
    }
}
