package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.service.MemberService;
import roomescape.service.dto.member.MemberLoginRequest;
import roomescape.service.dto.member.MemberTokenResponse;

@Controller
public class AuthController {
    private static final String COOKIE_NAME = "auth_token";

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
        ResponseCookie responseCookie = ResponseCookie.from(COOKIE_NAME, tokenResponse.token())
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @GetMapping("/signup")
    public String signUpPage() {
        return "signup";
    }
}
