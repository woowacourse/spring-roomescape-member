package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.LoggedIn;
import roomescape.domain.member.AuthenticatedMember;
import roomescape.service.member.MemberService;
import roomescape.service.member.dto.MemberLoginRequest;
import roomescape.service.member.dto.MemberResponse;
import roomescape.service.member.dto.MemberTokenResponse;

@Controller
public class AuthController {
    private static final String AUTH_COOKIE_NAME = "auth_token";

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody MemberLoginRequest request) {
        MemberTokenResponse tokenResponse = memberService.login(request);
        ResponseCookie responseCookie = ResponseCookie.from(AUTH_COOKIE_NAME, tokenResponse.token())
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(@LoggedIn AuthenticatedMember member) {
        MemberResponse response = new MemberResponse(member.getId(), member.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from(AUTH_COOKIE_NAME, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/signup")
    public String signUpPage() {
        return "signup";
    }
}
