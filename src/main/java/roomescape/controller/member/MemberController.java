package roomescape.controller.member;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.security.Permission;
import roomescape.domain.member.Role;
import roomescape.service.dto.member.MemberResponse;
import roomescape.service.member.MemberService;

import java.util.List;

@RestController
@RequestMapping("/members")
@Permission(role = Role.ADMIN)
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> memberRespons = memberService.findMembers().stream()
                .map(MemberResponse::of)
                .toList();
        return ResponseEntity.ok(memberRespons);
    }
}
