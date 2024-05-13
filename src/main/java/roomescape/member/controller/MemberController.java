package roomescape.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findMemberList() {
        List<Member> members = memberService.findMemberList();

        List<MemberResponse> memberResponse = changeToMemberResponses(members);

        return ResponseEntity.ok(memberResponse);
    }

    private MemberResponse changeToMemberResponse(Member member) {
        return new MemberResponse(member);
    }

    private List<MemberResponse> changeToMemberResponses(List<Member> members) {
        return members.stream()
                .map(this::changeToMemberResponse)
                .toList();
    }
}
