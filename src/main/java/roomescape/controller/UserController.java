package roomescape.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.controller.request.LoginUserRequest;
import roomescape.service.UserService;
import roomescape.service.param.LoginUserParam;
import roomescape.service.result.LoginUserResult;

@Controller
public class UserController {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginUserRequest loginUserRequest, HttpServletResponse response) {
        LoginUserParam loginUserParam = loginUserRequest.toServiceParam();
        LoginUserResult user = userService.login(loginUserParam);

        String accessToken = Jwts.builder()
                .subject(user.id().toString())
                .claim("name", user.name())
                .claim("email", user.email())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();

        response.addHeader("Keep-Alive", "timeout=60");
        response.addHeader("Content-Type", "application/json");

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
