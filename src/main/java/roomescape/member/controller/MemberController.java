package roomescape.member.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.SignUpSuccessResponse;
import roomescape.member.dto.UserLoginCheckResponse;
import roomescape.member.dto.UserLoginRequest;
import roomescape.member.dto.UserSignupRequest;
import roomescape.member.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<SignUpSuccessResponse> signup(@Valid @RequestBody UserSignupRequest userSignupRequest) {
        memberService.signup(userSignupRequest);
        return ResponseEntity.ok(new SignUpSuccessResponse("회원가입에 성공하였습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        String jwtToken = memberService.login(userLoginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, setCookieByToken(jwtToken).toString()).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<UserLoginCheckResponse> checkUserLogin(
            @CookieValue(name = "token", required = false) String token) {
        UserLoginCheckResponse userLoginCheckResponse = memberService.getUserNameFromToken(token);
        return ResponseEntity.ok(userLoginCheckResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, removeCookie("token").toString()).build();
    }

    private ResponseCookie setCookieByToken(String jwtToken) {
        return ResponseCookie.from("token", jwtToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
    }

    private ResponseCookie removeCookie(String name) {
        return ResponseCookie.from(name, null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
    }
}
