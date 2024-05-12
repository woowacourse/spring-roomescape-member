package roomescape.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.service.MemberService;
import roomescape.service.dto.response.MemberResponse;
import roomescape.web.api.dto.MemberListResponse;

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
                .map(member -> new MemberResponse(member.getId(), member.getName()))
                .toList();

        return ResponseEntity.ok(new MemberListResponse(memberResponses));
    }
}
