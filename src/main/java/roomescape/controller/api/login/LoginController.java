package roomescape.controller.api.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.AuthenticatedUser;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.AuthResponse;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.AuthService;
import roomescape.service.CookieService;
import roomescape.service.MemberService;

@RestController
public class LoginController {

    private final CookieService cookieService;
    private final AuthService authService;
    private final MemberService memberService;

    public LoginController(CookieService cookieService, AuthService authService, MemberService memberService) {
        this.cookieService = cookieService;
        this.authService = authService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        MemberResponse memberResponse = memberService.login(loginRequest);
        TokenRequest tokenRequest = new TokenRequest(memberResponse.email(), memberResponse.name());
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        Cookie cookie = cookieService.makeCookie(tokenResponse.token());
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
        Cookie cookie = cookieService.makeExpireCookie();
        response.addCookie(cookie);
    }
}
