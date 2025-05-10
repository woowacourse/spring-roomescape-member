package roomescape.presentation.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.application.service.AuthService;
import roomescape.domain.model.Member;
import roomescape.presentation.annotation.MemberAuthorization;
import roomescape.presentation.dto.request.LoginRequest;
import roomescape.presentation.dto.request.SignupRequest;
import roomescape.presentation.dto.response.LoginResponse;
import roomescape.presentation.dto.response.MemberNameResponse;
import roomescape.presentation.dto.response.SignupResponse;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request, false);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup/admin")
    public ResponseEntity<SignupResponse> signupForAdmin(@RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request, true);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Validated @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        String token = response.token();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Keep-Alive", "timeout=" + 60);
        headers.add("Set-Cookie", "token=" + token + "; Path=/; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> check(@MemberAuthorization Member member) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Keep-Alive", "timeout=" + 60);
        headers.add("Transfer-Encoding", "chunked");

        MemberNameResponse response = new MemberNameResponse(member.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "token=" + "; Path=/; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }
}
