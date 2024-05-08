package roomescape.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.controller.request.LoginRequest;
import roomescape.service.AuthService;
import roomescape.service.dto.AuthDto;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        AuthDto authDto = AuthDto.from(loginRequest);
        String accessToken = authService.createToken(authDto);
        Cookie cookie = createCookie(accessToken);
        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.getValue()).build();
        // TODO: HttpResponse 이용해서 쿠키 전달해보기?
        // cookie.addCookie(cookie);
    }

    private Cookie createCookie(String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
