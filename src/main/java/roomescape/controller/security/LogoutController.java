package roomescape.controller.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.infrastructure.TokenCookieProvider;

@RestController
@RequestMapping("/logout")
@Permission(role = Role.GUEST)
public class LogoutController {

    @PostMapping
    public ResponseEntity<Void> logout(Member member) {
        ResponseCookie cookie = TokenCookieProvider.createCookie("");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }
}
