package roomescape.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.service.MemberAuthService;
import roomescape.auth.service.dto.request.UserSignupRequest;

@RestController
public class SignupController {
    private final MemberAuthService service;

    public SignupController(MemberAuthService service) {
        this.service = service;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@RequestBody @Valid UserSignupRequest request) {
        service.signup(request);
        return ResponseEntity.ok().build();
    }
}
