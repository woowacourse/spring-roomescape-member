package roomescape.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.service.UserAuthService;
import roomescape.auth.service.dto.request.SignupRequest;

@RestController
public class SignupController {
    private final UserAuthService service;

    public SignupController(UserAuthService service) {
        this.service = service;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequest request) {
        service.signup(request);
        return ResponseEntity.ok().build();
    }
}
