package roomescape.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.auth.LoginCheckResponse;
import roomescape.dto.auth.LoginRequest;
import roomescape.service.auth.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private static int ONE_HOUR = 3600;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request,
                      HttpServletResponse servletResponse) {
        String token = authenticationService.createToken(request.email(), request.password());

        servletResponse.setContentType("application/json");
        servletResponse.setHeader("Keep-Alive", "timeout=60");

        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(ONE_HOUR);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        servletResponse.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@CookieValue(value = "token", required = false) String token,
                                                         HttpServletResponse response) {
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginCheckResponse(null));
        }
        String name = authenticationService.findNameByToken(token);

        response.setHeader("Connection", "keep-alive");
        response.setContentType("application/json");
        response.setHeader("Keep-Alive", "timeout=60");
        return ResponseEntity.ok(new LoginCheckResponse(name));
    }
}
