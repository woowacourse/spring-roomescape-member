package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.business.service.AuthService;
import roomescape.auth.presentation.request.LoginRequest;
import roomescape.auth.presentation.response.LoginCheckResponse;
import roomescape.global.auth.annotation.MemberId;
import roomescape.global.auth.jwt.dto.TokenDto;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody final LoginRequest loginRequest,
                                      final HttpServletResponse response) {
        TokenDto tokenDto = authService.login(loginRequest);
        addTokenToCookie(tokenDto, response);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@MemberId final Long memberId) {
        LoginCheckResponse response = authService.checkLogin(memberId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response) {
        Cookie deleteCookie = new Cookie("accessToken", null);
        deleteCookie.setPath("/");
        deleteCookie.setHttpOnly(true);
        deleteCookie.setMaxAge(0); // 즉시 삭제

        response.addCookie(deleteCookie);

        return ResponseEntity.noContent().build(); // 204 응답
    }

    private void addTokenToCookie(TokenDto tokenInfo, HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", tokenInfo.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
