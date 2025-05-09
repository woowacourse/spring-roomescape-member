package roomescape.controller.api;

import jakarta.servlet.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.auth.LoginRequestDto;
import roomescape.dto.auth.MemberResponse;
import roomescape.service.AuthService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String token = authService.publishToken(loginRequestDto);
        Cookie cookie = createCookie(token);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        MemberResponse memberResponse = authService.checkLoginStatus(token);
        return ResponseEntity.ok(memberResponse);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
