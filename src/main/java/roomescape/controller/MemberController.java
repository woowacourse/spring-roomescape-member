package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.member.LoginCheckResponse;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;
import roomescape.exception.AuthorizationException;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    private static final String TOKEN_KEY = "token";

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(HttpServletRequest request) {
        Cookie cookie = Arrays.stream(request.getCookies())
                .filter(element -> element.getName().equals(TOKEN_KEY))
                .findFirst()
                .orElseThrow(() -> new AuthorizationException("토큰이 쿠키에 담겨있지 않습니다."));

        String payload = authService.findPayload(cookie.getValue());
        LoginCheckResponse response = memberService.findAuthInfo(payload);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> signup(@RequestBody MemberSignupRequest signupRequest) {
        return ResponseEntity.created(URI.create("/login"))
                .body(memberService.add(signupRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        memberService.checkLoginInfo(loginRequest);
        LoginResponse loginResponse = authService.createToken(loginRequest);
        Cookie cookie = new Cookie(TOKEN_KEY, loginResponse.getAccessToken());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
