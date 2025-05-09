package roomescape.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
