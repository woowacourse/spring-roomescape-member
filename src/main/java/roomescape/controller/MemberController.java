package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.LoginUser;
import roomescape.constant.CommonKey;
import roomescape.domain.member.Member;
import roomescape.dto.member.LoginCheckResponse;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    public MemberController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> signup(@RequestBody MemberSignupRequest signupRequest) {
        return ResponseEntity.created(URI.create("/login"))
                .body(memberService.add(signupRequest));
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@LoginUser Member member) {
        return ResponseEntity.ok(new LoginCheckResponse(member));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        authService.checkLoginInfo(loginRequest);
        LoginResponse loginResponse = authService.createToken(loginRequest);
        ResponseCookie cookie = ResponseCookie.from(CommonKey.TOKEN_KEY.getKey(), loginResponse.getAccessToken())
                .path("/")
                .httpOnly(true)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from(CommonKey.TOKEN_KEY.getKey(), "")
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .build();
    }
}
