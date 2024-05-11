package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.domain.Member;
import roomescape.dto.RegisterRequestDto;
import roomescape.service.MemberService;

import java.net.URI;
import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> getMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok().body(members);
    }

    @PostMapping("/members")
    public ResponseEntity<Member> registerMember(@RequestBody RegisterRequestDto registerRequestDto) {
        Member member = memberService.register(registerRequestDto);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }
}
