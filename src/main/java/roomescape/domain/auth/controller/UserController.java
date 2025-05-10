package roomescape.domain.auth.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.auth.dto.UserCreateRequest;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.service.UserService;

@RestController
@RequestMapping("/members")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        final List<UserInfoResponse> responses = userService.getAll();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<UserInfoResponse> signUp(@Valid @RequestBody final UserCreateRequest userCreateRequest) {
        final UserInfoResponse register = userService.register(userCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(register);
    }
}
