package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.MemberRequest;
import roomescape.service.MemberService;

import java.net.URI;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Member> signup(@RequestBody MemberRequest memberRequest) {
        final Member member = memberService.join(memberRequest);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }
}
