package roomescape.controller.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.dto.FindMemberNameResponse;
import roomescape.domain.member.LoginMember;
import roomescape.service.MemberService;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<FindMemberNameResponse>> findAll() {
        List<LoginMember> members = memberService.findAll();
        List<FindMemberNameResponse> response = members.stream()
            .map(member -> new FindMemberNameResponse(member.name()))
            .toList();
        return ResponseEntity.ok(response);
    }
}
