package roomescape.web.controller;

import java.util.Arrays;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.AuthService;
import roomescape.service.InvalidAuthenticationException;
import roomescape.service.request.AuthenticationRequest;
import roomescape.service.response.AuthenticatedMemberInfo;

@RestController
class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationRequest> login(
            @Valid @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        String token = authService.authenticate(request);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(request);
    }

    //TODO: 예외 타입 개선, 컨트롤러 코드 개선
    @GetMapping("/login/check")
    public ResponseEntity<AuthenticatedMemberInfo> check(
            HttpServletRequest request
    ) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new InvalidAuthenticationException();
        }

        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("token"))
                .findFirst()
                .orElseThrow(InvalidAuthenticationException::new);

        return ResponseEntity.ok(authService.authorize(tokenCookie.getValue()));
    }
}
