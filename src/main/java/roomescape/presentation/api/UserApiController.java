package roomescape.presentation.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.application.AuthorizationException;
import roomescape.application.UserService;
import roomescape.domain.User;
import roomescape.presentation.Authenticated;
import roomescape.presentation.response.UserResponse;

@Controller
@RequestMapping("/users")
public class UserApiController {

    private final UserService userService;

    public UserApiController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers(@Authenticated final User requestedUser) {
        if (!requestedUser.isAdmin()) {
            throw new AuthorizationException("관리자만 접근 가능합니다.");
        }

        var users = userService.findAllUsers();
        var response = UserResponse.from(users);
        return ResponseEntity.ok(response);
    }
}
