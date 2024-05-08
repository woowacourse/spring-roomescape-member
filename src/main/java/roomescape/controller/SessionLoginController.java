package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.MemberService;
import roomescape.service.TokenResponse;

@RestController
@RequestMapping("/login")
public class SessionLoginController {

    //TODO 로그인 상태에서 /login 하면 redirect
    private final MemberService memberService;

    public SessionLoginController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> login(@RequestBody final MemberLoginRequest memberLoginRequest,
                                               final HttpServletResponse response) {
        // TODO cookie 예쁘게 굽기

        final TokenResponse token = memberService.createToken(memberLoginRequest);

        final Cookie cookie = new Cookie("token", token.assessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader("Keep-Alive", "timeout=600");
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> check(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        final String tokenEmail = extractTokenFromCookie(cookies);

        final MemberResponse member = memberService.findMemberByToken(tokenEmail);

        return ResponseEntity.ok(member);
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
