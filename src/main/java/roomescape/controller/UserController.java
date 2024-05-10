package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import roomescape.controller.request.UserLoginRequest;
import roomescape.model.User;
import roomescape.service.AuthService;
import roomescape.service.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginRequest request, HttpServletResponse response) {
        User user = userService.findUserByEmailAndPassword(request);
        Cookie cookie = authService.createCookieByUser(user);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
