package roomescape.controller.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.member.dto.LoginCheckResponse;
import roomescape.controller.member.dto.MemberLoginRequest;
import roomescape.controller.member.dto.TokenResponse;
import roomescape.service.MemberService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;

    public AuthController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid final MemberLoginRequest request,
                                               final HttpServletResponse response) {
        String token = memberService.login(request);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Connection", "keep-alive");
        headers.set("Keep-Alive", "timeout=60");

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TokenResponse(token));
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(final HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);
        LoginCheckResponse response = memberService.checkLogin(token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Connection", "keep-alive");
        headers.set("Keep-Alive", "timeout=60");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Connection", "keep-alive");
        headers.set("Keep-Alive", "timeout=60");

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
