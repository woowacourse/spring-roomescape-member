package roomescape.reservation.presentation.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.reservation.application.MemberService;
import roomescape.reservation.application.dto.info.MemberDto;
import roomescape.reservation.presentation.dto.response.MemberResponse;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberResponse> getAllMembers() {
        List<MemberDto> members = memberService.getAllMembers();
        return MemberResponse.from(members);
    }
}
