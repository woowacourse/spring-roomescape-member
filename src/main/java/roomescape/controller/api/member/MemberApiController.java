package roomescape.controller.api.member;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.controller.api.member.dto.MemberResponse;
import roomescape.controller.api.member.dto.MemberSignupRequest;
import roomescape.controller.helper.LoginMember;
import roomescape.exception.AuthorizationException;
import roomescape.model.Member;
import roomescape.service.MemberService;

@Controller
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService service;
    private final MemberService memberService;

    public MemberApiController(final MemberService service, final MemberService memberService) {
        this.service = service;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> addMember(@RequestBody @Valid final MemberSignupRequest request) {
        var member = service.add(request);
        return ResponseEntity.created(URI.create("/reservations/" + member.id())).body(member);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers(@LoginMember final Member requestedMember) {
        if (!requestedMember.isAdmin()) {
            throw new AuthorizationException("접근 권한이 없습니다. 관리자 권한이 필요한 요청입니다.");
        }
        var response = memberService.findAll();
        return ResponseEntity.ok().body(response);
    }
}
