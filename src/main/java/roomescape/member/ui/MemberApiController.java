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
import roomescape.member.application.MemberService;
import roomescape.member.domain.Member;
import roomescape.member.dto.LoginRequest;
import roomescape.member.application.AuthService;
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
    public ResponseEntity<LoginResponse> findLoginUser(@AuthenticationPrincipal Member user) {
        return ResponseEntity.ok()
                .body(new LoginResponse(user.name()));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> createToken(@RequestBody LoginRequest loginRequest) {
        Long userId = memberService.findIdByEmailAndPassword(loginRequest);
        String accessToken = authService.createToken(String.valueOf(userId));
        ResponseCookie cookie = ResponseCookie.from("token", accessToken)
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
