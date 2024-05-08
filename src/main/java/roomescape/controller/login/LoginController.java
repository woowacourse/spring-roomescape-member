package roomescape.controller.login;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.application.AuthService;
import roomescape.auth.dto.MemberResponse;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.infrastructure.AuthorizationExtractor;

@Controller
public class LoginController {

    public static final String TOKEN_NAME = "token";

    private final AuthService authService;
    private final AuthorizationExtractor authorizationExtractor;

    public LoginController(AuthService authService) {
        this.authService = authService;
        this.authorizationExtractor = new AuthorizationExtractor();
    }

    @GetMapping("/login")
    public String page() {
        return "login";
    }

    @PostMapping("/login")
    public void login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        String accessToken = authService.createToken(tokenRequest);

        Cookie cookie = new Cookie(TOKEN_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> findMyInfo(@Login LoginMember loginMember) {
        return ResponseEntity.ok().body(new MemberResponse(loginMember.id(), loginMember.name()));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        Arrays.stream(cookies)
                .filter(cookie -> TOKEN_NAME.equals(cookie.getName()))
                .forEach(cookie -> {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                });
    }
}
