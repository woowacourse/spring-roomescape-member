package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.domain.User;
import roomescape.service.TokenService;
import roomescape.service.UserService;
import roomescape.service.dto.request.LoginRequest;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;
    private final TokenService tokenService;

    public LoginController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<Void> postLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        User user = userService.findByEmail(loginRequest.email());
        Cookie cookie = new Cookie("token", tokenService.generateTokenOf(user));
        cookie.setMaxAge(60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
