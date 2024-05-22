package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.CookieService;
import roomescape.service.MemberService;

import java.util.List;

@RestController
public class LoginController {
    private final CookieService cookieService;
    private final MemberService memberService;

    public LoginController(final CookieService cookieService, final MemberService memberService) {
        this.cookieService = cookieService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody final TokenRequest tokenRequest,
                                               HttpServletResponse response) {
        TokenResponse tokenResponse = memberService.createToken(tokenRequest);
        response.addCookie(cookieService.createCookie(tokenResponse));
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> authorizeLogin(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        final String token = cookieService.extractTokenFromCookie(cookies);
        final MemberResponse response = memberService.findMemberByToken(token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response) {
        final Cookie cookie = cookieService.createEmptyCookie();
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> readMembers() {
        List<MemberResponse> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }

}
