package roomescape.member.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.controller.dto.LoginMemberResponse;
import roomescape.member.service.LoginMemberService;

@RequestMapping("/members")
@RestController
public class LoginMemberController {

    private final LoginMemberService loginMemberService;

    public LoginMemberController(final LoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LoginMemberResponse> findAllReservationTimes() {
        return loginMemberService.findAllLoginMembers()
                .stream()
                .map(LoginMemberResponse::from)
                .toList();
    }
}
