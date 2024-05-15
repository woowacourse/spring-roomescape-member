package roomescape.controller.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.LogInRequest;
import roomescape.dto.MemberPreviewResponse;
import roomescape.service.MemberService;

@RestController
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LogInRequest logInRequest) {
        String token = memberService.logIn(logInRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, token)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberPreviewResponse> loginCheck(Member member) {
        if (member == null) {
            return ResponseEntity.ok()
                    .build();
        }

        MemberPreviewResponse name = MemberPreviewResponse.from(member);

        return ResponseEntity.ok()
                .body(name);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        memberService.logout(response);

        return ResponseEntity.ok()
                .build();
    }
}
