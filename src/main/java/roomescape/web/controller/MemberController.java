package roomescape.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.MemberService;
import roomescape.service.request.MemberLoginRequest;
import roomescape.service.response.MemberResponse;
import roomescape.service.response.Token;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody MemberLoginRequest request, HttpServletResponse response) {
        Token token = memberService.login(request);

        Cookie cookie = new Cookie("token", token.value());
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> getMemberInfo(@CookieValue(name = "token") String token) {
        MemberResponse response = memberService.getMemberInfo(token);

        return ResponseEntity.ok(response);
    }
}
