package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.dto.request.UserCreateRequest;
import roomescape.dto.response.TokenResponse;
import roomescape.dto.response.UserResponse;
import roomescape.service.UserService;

@Controller
public class UserPageController {
    private final UserService userService;

    public UserPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserIndexPage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String showUserReservationPage() {
        return "reservation";
    }

    @GetMapping("/login")
    public String showUserLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> showUserLoginPage(HttpServletResponse response, @RequestBody UserCreateRequest userCreateRequest) {
        TokenResponse tokenResponse = userService.createToken(userCreateRequest);
        Cookie cookie = new Cookie("token", tokenResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<UserResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        UserResponse userResponse = userService.createUserResponse(cookies);

        return ResponseEntity.ok().body(userResponse);
    }
}
