package roomescape.controller;

import jakarta.servlet.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.controller.request.LoginRequest;
import roomescape.controller.response.LoginResponse;
import roomescape.exception.AuthorizationException;
import roomescape.service.AuthService;
import roomescape.service.dto.AuthDto;
import roomescape.service.dto.MemberInfo;

import java.util.Arrays;
import java.util.Optional;

@Controller
public class AuthController {

    private static final String TOKEN = "token";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        AuthDto authDto = AuthDto.from(loginRequest);
        String accessToken = authService.createToken(authDto);
        Cookie cookie = createCookie(TOKEN, accessToken);
        System.out.println(accessToken);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> checkLogin(HttpServletRequest request) {
        Cookie token = findCookieByKey(request.getCookies(), TOKEN).orElseThrow(AuthorizationException::new);
        MemberInfo loginMember = authService.checkToken(token.getValue());
        LoginResponse response = new LoginResponse(loginMember.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private Optional<Cookie> findCookieByKey(Cookie[] cookies, String key) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(key))
                .findFirst();
    }
}
