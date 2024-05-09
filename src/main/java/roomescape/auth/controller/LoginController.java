package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.service.AuthService;
import roomescape.member.domain.Member;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String login() {
        return "/login";
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Void> tokenLogin(HttpServletResponse response, @RequestBody TokenRequest tokenRequest) {
        String accessToken = authService.createToken(tokenRequest);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    @ResponseBody
    public ResponseEntity<TokenResponse> checkLogin(Member member) {
        TokenResponse tokenResponse = new TokenResponse(member);

        return ResponseEntity.ok(tokenResponse);
    }
}
