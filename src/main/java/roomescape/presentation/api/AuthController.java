package roomescape.presentation.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.application.AuthService;
import roomescape.presentation.AuthenticationPrincipal;
import roomescape.presentation.dto.request.LoginMember;
import roomescape.presentation.dto.request.LoginRequest;
import roomescape.presentation.dto.response.MemberResponse;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = authService.login(loginRequest);
        createCookie(response, accessToken);
        return ResponseEntity.ok().build();
    }

    private void createCookie(final HttpServletResponse response, final String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60);
        response.addCookie(cookie);
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(@AuthenticationPrincipal LoginMember loginMember) {
        MemberResponse memberResponse = MemberResponse.from(loginMember);
        return ResponseEntity.ok().body(memberResponse);
    }
}
