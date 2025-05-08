package roomescape.member.presentation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.application.service.MemberService;
import roomescape.member.infrastructure.CookieAuthorizationExtractor;
import roomescape.member.presentation.dto.MemberResponse;
import roomescape.member.presentation.dto.TokenRequest;

@RestController
public class LoginController {
    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
        this.authorizationExtractor = new CookieAuthorizationExtractor();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody TokenRequest tokenRequest
    ) {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, memberService.createToken(tokenRequest))
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> loginCheck(
            HttpServletRequest request
    ) {
        return ResponseEntity.ok().body(
                memberService.findByToken(authorizationExtractor.extract(request))
        );
    }
}
