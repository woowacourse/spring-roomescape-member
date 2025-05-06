package roomescape.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthRequired;
import roomescape.auth.Role;
import roomescape.business.model.entity.User;
import roomescape.business.model.vo.UserRole;
import roomescape.business.service.UserService;
import roomescape.presentation.dto.request.RegisterRequest;
import roomescape.presentation.dto.response.UserResponse;

import java.util.List;

@RestController
public class UserApiController {

    private final UserService userService;

    public UserApiController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/members")
    @AuthRequired
    @Role(UserRole.ADMIN)
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<User> users = userService.getAll();
        List<UserResponse> responses = UserResponse.from(users);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/members")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request.name(), request.email(), request.password());

        return ResponseEntity.noContent().build();
    }
}
