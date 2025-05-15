package roomescape.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.member.MemberResponse;
import roomescape.entity.Member;
import roomescape.service.MemberService;

import java.util.List;

@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List<MemberResponse> getMembers() {
        List<Member> members = memberService.getAllMembers();
        return members.stream().
                map(MemberResponse::from)
                .toList();
    }
}
