package roomescape.controller.api.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.AuthenticatedUser;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.AuthResponse;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

import java.util.Optional;

@RestController
public class LoginController {

    private final AuthService authService;
    private final MemberService memberService;

    public LoginController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        MemberResponse memberResponse = memberService.login(loginRequest);
        Cookie cookie = makeCookie(memberResponse);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public LoginCheckResponse loginCheck(@AuthenticatedUser AuthResponse authResponse) {
        MemberResponse memberResponse = memberService.findMemberByEmail(authResponse.email());
        return new LoginCheckResponse(memberResponse.name());
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    private Cookie makeCookie(MemberResponse memberResponse) {
        TokenRequest tokenRequest = new TokenRequest(memberResponse.email(), memberResponse.name());
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        Cookie cookie = new Cookie("token", tokenResponse.token());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
