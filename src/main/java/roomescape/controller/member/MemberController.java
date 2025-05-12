package roomescape.controller.member;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.MemberRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.service.memeber.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse addMember(@RequestBody MemberRequest memberRequest,
                                    HttpServletResponse response) {
        MemberResponse memberResponse = memberService.addMember(memberRequest);
        response.setHeader("Location", "/reservations/" + memberResponse.id());
        return memberResponse;
    }
}
