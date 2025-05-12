package roomescape.member.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.controller.annotation.Admin;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.MemberService;

@Admin
@RequestMapping("/admin/members")
@RestController
public class AdminMemberController {

    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberResponse> getMembers() {
        return memberService.findAll();
    }

}
