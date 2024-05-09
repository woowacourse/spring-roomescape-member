package roomescape.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.service.MemberService;
import roomescape.service.TokenService;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final TokenService tokenService;

    public MemberController(MemberService memberService, TokenService tokenService) {
        this.memberService = memberService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        long userId = memberService.login(loginRequest);
        LocalDateTime now = LocalDateTime.now();
        String token = tokenService.createToken(userId, now, Duration.between(now, now.plusHours(1)));
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
