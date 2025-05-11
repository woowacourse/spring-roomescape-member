package roomescape.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.dto.request.LoginRequest;
import roomescape.user.dto.response.BriefUserResponse;
import roomescape.user.dto.response.LoginCheckResponse;
import roomescape.user.domain.UserPrinciple;
import roomescape.user.service.LoginService;

@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = loginService.login(loginRequest);
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@CookieValue(name = "token") String token) {
        return ResponseEntity.ok(loginService.checkUserByToken(token));
    }

    @GetMapping("/login/check-v2")
    public ResponseEntity<String> test(UserPrinciple userPrinciple) {
        return ResponseEntity.ok(userPrinciple.name() + " " + userPrinciple.email());
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<BriefUserResponse>> getAllUsers() {
        List<BriefUserResponse> users = loginService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
