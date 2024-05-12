package roomescape.web.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.token.TokenParser;
import roomescape.domain.token.TokenProvider;
import roomescape.service.MemberService;
import roomescape.service.dto.response.MemberResponse;
import roomescape.web.api.dto.LoginRequest;
import roomescape.web.exception.AuthorizationException;

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

        String payload = tokenParser.getEmail(token);

        return ResponseEntity.ok(new MemberResponse(payload));
    }
}
