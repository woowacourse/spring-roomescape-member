package roomescape.web.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import roomescape.service.MemberService;
import roomescape.service.security.JwtUtils;
import roomescape.web.dto.request.LoginRequest;
import roomescape.web.dto.response.MemberResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private static final String TOKEN_COOKIE_KEY_NAME = "token";

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        String jwtToken = memberService.login(request);

        return ResponseEntity.ok()
                .header(SET_COOKIE, jwtToken)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkAuthenticated(
            @CookieValue(value = TOKEN_COOKIE_KEY_NAME, defaultValue = "") String token) {
        MemberResponse response = new MemberResponse(JwtUtils.decodeName(token));

        return ResponseEntity.ok()
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie token = new Cookie(TOKEN_COOKIE_KEY_NAME, null);
        token.setMaxAge(0);
        token.setPath("/");
        response.addCookie(token);

        return ResponseEntity.ok()
                .build();
    }
}
