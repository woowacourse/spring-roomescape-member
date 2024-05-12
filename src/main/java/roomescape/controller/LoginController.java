package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.User;
import roomescape.dto.request.UserCreateRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.dto.response.UserLoginResponse;
import roomescape.dto.response.UserResponse;
import roomescape.service.LoginService;

@Controller
@RequestMapping("/login")
public class LoginController {

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String showUserLoginPage() {
        return "login";
    }

    @PostMapping
    public ResponseEntity<TokenResponse> showUserLoginPage(HttpServletResponse response, @RequestBody UserCreateRequest userCreateRequest) {
        TokenResponse tokenResponse = loginService.createToken(userCreateRequest);
        Cookie cookie = new Cookie("token", tokenResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/check")
    public ResponseEntity<UserLoginResponse> checkLogin(User user) {
        return ResponseEntity.ok().body(UserLoginResponse.from(user));
    }
}
