package roomescape.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.dto.SignUpRequest;
import roomescape.user.dto.SignUpResponse;
import roomescape.user.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/members")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse signUpResponse = userService.registerUser(request);
        URI location = URI.create("/members/" + signUpResponse.getId());
        return ResponseEntity.created(location).body(signUpResponse);
    }
}
