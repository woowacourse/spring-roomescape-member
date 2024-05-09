package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.controller.request.LoginRequest;
import roomescape.controller.response.LoginResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.Member;
import roomescape.service.AuthService;
import roomescape.service.dto.AuthDto;

import java.util.Arrays;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        AuthDto authDto = AuthDto.from(loginRequest);
        String accessToken = authService.createToken(authDto);
        Cookie cookie = createCookie(accessToken);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private Cookie createCookie(String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> checkLogin(HttpServletRequest request) {
        Cookie token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(); // -> return 401
        Member loginMember = authService.checkToken(token.getValue());
        LoginResponse response = new LoginResponse(loginMember.getName());
        return ResponseEntity.ok(response);
    }
}
