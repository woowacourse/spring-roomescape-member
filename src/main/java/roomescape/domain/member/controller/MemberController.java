package roomescape.domain.member.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.dto.request.MemberRequestDto;
import roomescape.domain.member.dto.response.MemberResponseDto;
import roomescape.domain.member.dto.response.MemberResponseDtoOfNames;
import roomescape.domain.member.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/members")
    public MemberResponseDto saveMember(@RequestBody MemberRequestDto memberRequestDto) {
        return memberService.saveMember(memberRequestDto);
    }

    @GetMapping("/members")
    public List<MemberResponseDtoOfNames> getMembers() {
        return memberService.getAllMembers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/admin/members")
    public MemberResponseDto saveAdmin(@RequestBody MemberRequestDto memberRequestDto) {
        return memberService.saveAdmin(memberRequestDto);
    }
}
