package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.dto.LoginRequest;
import roomescape.domain.dto.SignupRequest;
import roomescape.domain.dto.SignupResponse;
import roomescape.domain.dto.TokenResponse;
import roomescape.service.MemberService;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        final SignupResponse response = memberService.createUser(signupRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        final TokenResponse tokenResponse = memberService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
