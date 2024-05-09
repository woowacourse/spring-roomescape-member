package roomescape.domain.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.member.dto.LoginRequest;
import roomescape.domain.member.dto.MemberResponse;
import roomescape.domain.member.service.LoginService;
import roomescape.global.auth.AuthUser;

@RestController
@RequestMapping(("/login"))
public class MemberLoginRestController {

    private static final String TOKEN = "token";

    private final LoginService loginService;

    public MemberLoginRestController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String accessToken = loginService.login(request);

        Cookie cookie = new Cookie(TOKEN, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(AuthUser authUser) {
        return ResponseEntity.ok(new MemberResponse(authUser.id(), authUser.name()));
    }
}
