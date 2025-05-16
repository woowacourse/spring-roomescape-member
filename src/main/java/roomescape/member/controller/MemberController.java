package roomescape.member.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.controller.dto.CreateMemberRequest;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;
import roomescape.member.service.dto.CreateMemberServiceRequest;

@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse createMember(@RequestBody CreateMemberRequest request) {

        final CreateMemberServiceRequest serviceRequest = request.toCreateMemberServiceRequest();
        final Member savedMember = memberService.addMember(serviceRequest);

        return MemberResponse.from(savedMember);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MemberResponse> findAllMembers() {
        return memberService.findAllMembers()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }
}
