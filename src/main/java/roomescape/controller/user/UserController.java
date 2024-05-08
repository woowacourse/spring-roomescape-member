package roomescape.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.SignupResponse;
import roomescape.service.user.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> save(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(userService.save(signupRequest));
    }
}
