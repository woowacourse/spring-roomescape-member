package roomescape.controller.member;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.request.SignupRequest;
import roomescape.controller.dto.response.MemberResponse;
import roomescape.controller.dto.response.SignupResponse;
import roomescape.service.member.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<SignupResponse> save(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(memberService.save(signupRequest));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMemberNames() {
        return ResponseEntity.ok(memberService.getAllMemberNames());
    }
}
