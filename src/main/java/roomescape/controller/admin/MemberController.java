package roomescape.controller.admin;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.response.MemberProfileResponse;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List<MemberProfileResponse> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return members.stream()
                .map(MemberProfileResponse::new)
                .toList();
    }
}
