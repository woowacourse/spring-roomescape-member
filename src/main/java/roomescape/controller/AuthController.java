package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.MemberService;
import roomescape.application.dto.response.MemberResponse;
import roomescape.application.dto.response.TokenResponse;
import roomescape.auth.AuthenticationPrincipal;
import roomescape.controller.dto.LoginRequest;

@RestController
public class AuthController {
    private static final String TOKEN_KEY_NAME = "token";
    private static final String EMPTY_TOKEN = "";

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = memberService.authenticateMember(request.toTokenCreationRequest());
        ResponseCookie cookie = ResponseCookie.from(TOKEN_KEY_NAME, response.token())
                .httpOnly(true)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public MemberResponse check(@AuthenticationPrincipal long memberId) {
        return memberService.getMemberById(memberId);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from(TOKEN_KEY_NAME, EMPTY_TOKEN)
                .maxAge(0)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
