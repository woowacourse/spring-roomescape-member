package roomescape.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.SignupResponse;
import roomescape.service.member.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<SignupResponse> save(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(memberService.save(signupRequest));
    }
}
