package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.service.MemberService;
import roomescape.service.dto.member.MemberLoginRequest;
import roomescape.service.dto.member.MemberResponse;
import roomescape.service.dto.member.MemberTokenResponse;
import roomescape.utils.TokenManager;

@Controller
public class AuthController {
    private static final String COOKIE_NAME = "auth_token";

    private final MemberService memberService;
    private final TokenManager tokenManager;

    public AuthController(MemberService memberService, TokenManager tokenManager) {
        this.memberService = memberService;
        this.tokenManager = tokenManager;
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

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow();

        MemberResponse response = tokenManager.getMemberResponseFromToken(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/signup")
    public String signUpPage() {
        return "signup";
    }
}
