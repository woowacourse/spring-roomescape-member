package roomescape.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginMember;
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
    public ResponseEntity<TokenResponse> login(@RequestBody final TokenRequest tokenRequest) {
        TokenResponse tokenResponse = memberService.createToken(tokenRequest);
        ResponseCookie responseCookie = cookieService.createCookie(tokenResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .body(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> authorizeLogin(final LoginMember loginMember) {
        final MemberResponse response = memberService.checkLogin(loginMember);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        final ResponseCookie responseCookie = cookieService.createEmptyCookie();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> readMembers() {
        List<MemberResponse> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }
}
