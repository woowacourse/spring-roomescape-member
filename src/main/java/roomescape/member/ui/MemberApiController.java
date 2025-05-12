package roomescape.member.ui;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.Token;
import roomescape.auth.ui.AuthenticationPrincipal;
import roomescape.member.application.MemberService;
import roomescape.member.domain.Member;
import roomescape.auth.application.AuthService;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.LoginResponse;
import roomescape.member.dto.MemberResponse;

@RestController
public class MemberApiController {

    private final AuthService authService;
    private final MemberService memberService;

    public MemberApiController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> findAll() {
        return ResponseEntity.ok()
                .body(memberService.findAll());
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> findLoginUser(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok()
                .body(new LoginResponse(member.name()));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> createToken(@RequestBody LoginRequest loginRequest) {
        Token accessToken = authService.login(loginRequest);
        ResponseCookie authCookie = createAuthCookie(accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private ResponseCookie createAuthCookie(Token accessToken) {
        return ResponseCookie.from("token", accessToken.value())
                .httpOnly(true)
                .path("/")
                .build();
    }
}
