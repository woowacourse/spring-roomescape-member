package roomescape.controller.member;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.member.MemberResponse;
import roomescape.service.memeber.MemberService;

@RestController
@RequestMapping("/admin")
public class AdminMemberController {

    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List<MemberResponse> readAvailableReservationTimes() {
        return memberService.readAll();
    }
}
