package roomescape.controller.api.reservation;

import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberResponse> findAll() {
        return memberService.findAll();
    }

    @PostMapping
    public MemberResponse signup(@RequestBody SignupRequest signupRequest) {
        return memberService.create(signupRequest);
    }
}
