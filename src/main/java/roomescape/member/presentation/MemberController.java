package roomescape.member.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.MemberResponses;
import roomescape.member.application.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<MemberResponses> findAllMembers() {
        MemberResponses memberResponses = memberService.findAll();
        return ResponseEntity.ok()
                .body(memberResponses);
    }
}
