package roomescape.web.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.auth.TokenProvider;
import roomescape.service.MemberService;
import roomescape.web.exception.AuthorizationException;

@RestController
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    public AuthController(MemberService memberService, TokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = memberService.login(request.email(), request.password());

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .path("/")
                .httpOnly(true)
                .maxAge(3600)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        String token = tokenProvider.extractToken(request.getCookies())
                .orElseThrow(AuthorizationException::new);

        String payload = tokenProvider.getEmail(token);

        return ResponseEntity.ok(new MemberResponse(payload));
    }

    public record LoginRequest(String email, String password) {
    }

    public record MemberResponse(String name) {
    }
}
