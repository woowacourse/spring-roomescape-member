package roomescape.auth.ui;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.application.AuthService;
import roomescape.auth.domain.AuthRole;
import roomescape.auth.domain.RequiresRole;
import roomescape.auth.ui.dto.CheckAccessTokenResponse;
import roomescape.auth.ui.dto.CreateAccessTokenRequest;
import roomescape.member.domain.Member;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> createAccessToken(
            @RequestBody final CreateAccessTokenRequest request, HttpServletResponse response
    ) {
        final String authToken = authService.createAccessToken(request);

        final ResponseCookie cookie = ResponseCookie.from("token", authToken)
                .path("/")
                .httpOnly(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckAccessTokenResponse> checkAccessToken(final Member member) {
        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CheckAccessTokenResponse("Unauthorized"));
        }
        return ResponseEntity.ok(new CheckAccessTokenResponse(member.getName()));
    }

    @PostMapping("/logout")
    @RequiresRole(authRoles = {AuthRole.ADMIN, AuthRole.MEMBER})
    public ResponseEntity<Void> logout(@CookieValue(value = "token") final String accessToken) {
        log.info("Logout accessToken: {}", accessToken);

        final ResponseCookie cookie = ResponseCookie.from("token", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
