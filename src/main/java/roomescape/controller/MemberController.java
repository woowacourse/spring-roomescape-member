package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.domain.Member;
import roomescape.service.MemberService;

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
}
