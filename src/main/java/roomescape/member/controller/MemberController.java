package roomescape.member.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.service.MemberService;

@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService loginMemberService;

    public MemberController(final MemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MemberResponse> findAllLoginMembers() {
        return loginMemberService.findAllLoginMembers()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }
}
