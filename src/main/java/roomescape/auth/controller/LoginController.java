package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.controller.dto.CheckLoginResponse;
import roomescape.auth.controller.dto.LoginRequest;
import roomescape.auth.service.AuthService;
import roomescape.auth.service.dto.CreateTokenServiceRequest;
import roomescape.auth.util.CookieHandler;
import roomescape.member.domain.Member;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void tokenLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        final CreateTokenServiceRequest serviceRequest = loginRequest.toCreateTokenServiceRequest();

        final String token = authService.createToken(serviceRequest);
        final ResponseCookie cookie = CookieHandler.createCookieFromToken(token);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .location(URI.create("/"))
                .header("Set-Cookie", CookieHandler.createLogoutCookie().toString())
                .build();
    }

    @GetMapping("/login/check")
    public CheckLoginResponse checkLogin(HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        final String token = CookieHandler.extractTokenFromCookies(cookies);
        final Member member = authService.getMemberByToken(token);

        return CheckLoginResponse.from(member);
    }
}
