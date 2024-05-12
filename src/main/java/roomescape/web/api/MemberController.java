package roomescape.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.service.MemberService;
import roomescape.web.api.dto.MemberListResponse;
import roomescape.web.api.dto.MemberResponse;

import java.util.List;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<MemberListResponse> findAll() {
        List<Member> members = memberService.findAll();

        List<MemberResponse> memberResponses = members.stream()
                .map(Member::getId)
                .map(MemberResponse::new)
                .toList();

        return ResponseEntity.ok(new MemberListResponse(memberResponses));
    }
}
