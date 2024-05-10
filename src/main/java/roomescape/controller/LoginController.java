package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.UserCreateRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.dto.response.UserResponse;
import roomescape.service.LoginService;

@RestController
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
    public ResponseEntity<UserResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        UserResponse userResponse = loginService.createUserResponse(cookies);

        return ResponseEntity.ok().body(userResponse);
    }
}
