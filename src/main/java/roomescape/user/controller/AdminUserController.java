package roomescape.user.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.dto.response.UserSelectElementResponse;
import roomescape.user.service.UserService;

@RequestMapping("/admin/users")
@RestController
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserSelectElementResponse>> getAllUserSelectElements() {
        List<UserSelectElementResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
