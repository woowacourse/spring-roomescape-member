package roomescape.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.controller.dto.SignupRequest;
import roomescape.member.service.MemberService;

import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List<MemberResponse> getMembers() {
        return memberService.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/members")
    public MemberResponse signup(@RequestBody SignupRequest request) {
        return memberService.add(request);
    }

}
