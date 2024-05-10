package roomescape.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.service.LoginMemberService;
import roomescape.service.dto.LoginMemberResponse;

@Controller //rename
public class MemberController {
    private final LoginMemberService loginMemberService;

    public MemberController(LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @GetMapping("/reservation") //이동 ^^
    public String findReservationPage() {
        return "reservation";
    }

    @GetMapping("/members")
    @ResponseBody //restController 로 변경
    public List<LoginMemberResponse> findAllMembers() {
        return loginMemberService.findAll();
    }
}
