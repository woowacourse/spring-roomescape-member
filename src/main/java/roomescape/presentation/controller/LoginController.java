package roomescape.presentation.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.application.service.MemberService;
import roomescape.presentation.dto.request.TokenRequest;

@Controller
public class LoginController {

    private final MemberService memberService;

    public LoginController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Validated @RequestBody TokenRequest request) {
        String accessToken = memberService.login(request);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Keep-Alive", "timeout=" + 60);
        headers.add("Set-Cookie", "token=" + accessToken + "; Path=/; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }
}
