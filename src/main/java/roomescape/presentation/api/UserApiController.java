package roomescape.presentation.api;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.application.AuthorizationException;
import roomescape.application.UserService;
import roomescape.domain.User;
import roomescape.presentation.Authenticated;
import roomescape.presentation.request.SignupRequest;
import roomescape.presentation.response.UserResponse;

@Controller
@RequestMapping("/users")
public class UserApiController {

    private final UserService userService;

    public UserApiController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody @Valid final SignupRequest request) {
        var user = userService.register(request.email(), request.password(), request.name());
        var response = UserResponse.from(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(@Authenticated final User requestedUser) {
        if (!requestedUser.isAdmin()) {
            throw new AuthorizationException("관리자만 접근 가능합니다.");
        }

        var users = userService.findAllUsers();
        var response = UserResponse.from(users);
        return ResponseEntity.ok(response);
    }
}
