package roomescape.member.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.controller.dto.MemberInfoResponse;
import roomescape.member.service.AuthService;
import roomescape.member.service.dto.LoginInfo;
import roomescape.member.service.dto.TokenResponse;
import roomescape.member.service.dto.MemberInfo;

@RestController
public class MemberController {

    private final AuthService authService;

    public MemberController(final AuthService authService) {
        this.authService = authService;
    }

    /**
     * TODO
     * 이미 쿠키에 토큰이 존재한다면?
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginInfo request) {
        TokenResponse tokenResponse = authService.tokenLogin(request);
        ResponseCookie cookie = ResponseCookie.from("token", tokenResponse.token())
                .httpOnly(true)
                .path("/")
                .build();
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberInfoResponse> getLoginMemberInfo(@CookieValue("token") String token) {
        MemberInfo memberInfo = authService.getMemberInfoByToken(token);
        return ResponseEntity.ok().body(new MemberInfoResponse(memberInfo.name()));
    }
}
