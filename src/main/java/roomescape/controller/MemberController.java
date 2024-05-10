package roomescape.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.LoginMemberService;
import roomescape.service.dto.LoginMemberResponse;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final LoginMemberService loginMemberService;

    public MemberController(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @GetMapping
    public List<LoginMemberResponse> findAllMembers() {
        return loginMemberService.findAll();
    }
}
