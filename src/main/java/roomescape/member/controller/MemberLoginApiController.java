package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.annotaions.Login;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.MemberLoginService;

@RestController
public class MemberLoginApiController {

    public static final String COOKIE_TOKEN_KEY = "token";
    public static final int COOKIE_MAX_AGE = 3600;
    private final MemberLoginService memberLoginService;

    public MemberLoginApiController(MemberLoginService memberLoginService) {
        this.memberLoginService = memberLoginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberLoginRequest memberLoginRequest, HttpServletResponse response) {
        String token = memberLoginService.createMemberToken(memberLoginRequest);
        Cookie cookie = new Cookie(COOKIE_TOKEN_KEY, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> loginCheck(@Login LoginMember loginMember) {
        MemberResponse memberResponse = memberLoginService.findMemberNameByLoginMember(loginMember);

        return ResponseEntity.ok(memberResponse);
    }
}
