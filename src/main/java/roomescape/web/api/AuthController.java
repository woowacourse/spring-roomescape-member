package roomescape.web.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.MemberService;
import roomescape.service.dto.response.MemberResponse;
import roomescape.web.api.dto.LoginRequest;
import roomescape.web.api.token.TokenParser;
import roomescape.web.api.token.TokenProvider;
import roomescape.web.exception.AuthenticationException;

@RestController
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;

    public AuthController(MemberService memberService, TokenProvider tokenProvider, TokenParser tokenParser) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
        this.tokenParser = tokenParser;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
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
                .orElseThrow(AuthenticationException::new);

        Long id = tokenParser.getId(token);
        String payload = tokenParser.getEmail(token);

        return ResponseEntity.ok(new MemberResponse(id, payload));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .build();
    }
}
