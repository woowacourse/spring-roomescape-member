package roomescape.presentation.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.UserService;
import roomescape.presentation.dto.request.RegisterRequest;

@RestController
public class UserApiController {

    private final UserService userService;

    public UserApiController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request.name(), request.email(), request.password());

        return ResponseEntity.noContent().build();
    }
}
