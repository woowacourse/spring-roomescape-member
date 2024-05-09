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
    public ResponseEntity<List<MemberResponse>> findAll() {
        final List<Member> members = memberService.readAll();

        final List<MemberResponse> memberResponses = changeToMemberResponses(members);

        return ResponseEntity.ok(memberResponses);
    }

    private MemberResponse changeToMemberResponse(final Member member) {
        return new MemberResponse(member);
    }

    private List<MemberResponse> changeToMemberResponses(final List<Member> members) {
        return members.stream()
                .map(this::changeToMemberResponse)
                .toList();
    }
}
