package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthorizationExtractor;
import roomescape.auth.CookieAuthorizationExtractor;
import roomescape.domain.dto.*;
import roomescape.service.MemberService;

@RestController
public class MemberController {
    private final AuthorizationExtractor<String> authorizationExtractor;
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.authorizationExtractor = new CookieAuthorizationExtractor();
        this.memberService = memberService;
    }

    @PostMapping("/members")
    ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        final SignupResponse response = memberService.createUser(signupRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        final TokenResponse tokenResponse = memberService.login(loginRequest);
        ResponseCookie responseCookie = ResponseCookie.from(authorizationExtractor.TOKEN_NAME, tokenResponse.accessToken())
                .httpOnly(true)
                .path("/")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @GetMapping("/login/check")
    ResponseEntity<LoginResponse> loginCheck(HttpServletRequest request) {
        String accessToken = authorizationExtractor.extract(request);
        final LoginResponse loginResponse = memberService.loginCheck(accessToken);
        return ResponseEntity.ok(loginResponse);
    }
}
