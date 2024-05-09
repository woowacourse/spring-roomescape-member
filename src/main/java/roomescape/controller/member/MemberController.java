package roomescape.controller.member;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.member.dto.MemberLoginResponse;
import roomescape.domain.Member;
import roomescape.service.MemberService;

import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List<MemberLoginResponse> getMembers() {
        final List<Member> members = memberService.findAll();
        return members.stream()
                .map(member -> new MemberLoginResponse(member.getId(), member.getName()))
                .toList();
    }
}
