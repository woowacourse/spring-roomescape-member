package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.dto.request.CreateMemberRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateMemberRequest request) {
        Member createdMember = memberService.signUp(request)
                .orElseThrow(() -> new IllegalArgumentException("이미 존재하는 회원입니다."));

        return ResponseEntity.created(URI.create("/members/" + createdMember.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> readAll() {
        List<Member> members = memberService.readMembers();
        List<MemberResponse> memberResponses = members.stream()
                .map(MemberResponse::from)
                .toList();

        return ResponseEntity.ok(memberResponses);
    }
}
