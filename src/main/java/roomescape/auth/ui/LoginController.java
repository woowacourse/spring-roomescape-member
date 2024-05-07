package roomescape.auth.ui;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.auth.application.AuthService;
import roomescape.auth.dto.TokenRequest;

@RequestMapping("/login")
@Controller
public class LoginController {
    private final AuthService authService;
//    private final BearerAuthorizationExtractor authorizationExtractor;

    public LoginController(AuthService authService) {
        this.authService = authService;
//        this.authorizationExtractor = new BearerAuthorizationExtractor();
    }


    @GetMapping
    public String page() {
        return "login";
    }

    @PostMapping
    public void login(@RequestBody TokenRequest tokenRequest, HttpServletResponse response) {
        String accessToken = authService.createToken(tokenRequest);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

//    @GetMapping("/members/me/token")
//    public ResponseEntity<MemberResponse> findMyInfo(HttpServletRequest request) {
//        String token = authorizationExtractor.extract(request);
//        MemberResponse member = authService.findMemberByToken(token);
//        return ResponseEntity.ok().body(member);
//    }
}
