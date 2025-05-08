package roomescape.member.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.application.MemberService;
import roomescape.member.presentation.dto.TokenRequest;
import roomescape.member.presentation.dto.TokenResponse;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody TokenRequest tokenRequest
    ){
        return ResponseEntity.ok()
                .header("Set-Cookie", memberService.createToken(tokenRequest))
                .build();
    }
}
