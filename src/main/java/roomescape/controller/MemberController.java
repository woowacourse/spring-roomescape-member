package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        final TokenResponse tokenResponse = memberService.login(loginRequest);
        Cookie cookie = new Cookie(authorizationExtractor.TOKEN_NAME, tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    ResponseEntity<LoginResponse> loginCheck(HttpServletRequest request) {
        String accessToken = authorizationExtractor.extract(request);
        final LoginResponse loginResponse = LoginResponse.from(memberService.getMemberInfo(accessToken));
        return ResponseEntity.ok(loginResponse);
    }
}
