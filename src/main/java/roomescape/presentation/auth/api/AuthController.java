package roomescape.presentation.auth.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.auth.AuthService;
import roomescape.config.AccessToken;
import roomescape.config.CurrentLoginMember;
import roomescape.config.LoginMember;
import roomescape.presentation.auth.dto.LoginCheckResponseDto;
import roomescape.presentation.auth.dto.LoginRequestDto;
import roomescape.presentation.auth.dto.MemberRequestDto;

@RestController
public final class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        Long memberId = authService.registerMember(memberRequestDto);
        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                      HttpServletResponse response) {
        AccessToken accessToken = authService.login(loginRequestDto);
        setCookie(response, accessToken.getValue());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponseDto> checkLogin(@CurrentLoginMember LoginMember loginMember) {
        return ResponseEntity.ok(new LoginCheckResponseDto(authService.checkLogin(loginMember)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        clearCookie(response);
        return ResponseEntity.ok().build();
    }

    private void setCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setAttribute("SameSite", "Strict");
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
