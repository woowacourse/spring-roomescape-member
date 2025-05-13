package roomescape.presentation.member.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.member.MemberService;
import roomescape.presentation.member.dto.MemberResponseDto;

@RestController
public final class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        List<MemberResponseDto> members = memberService.getMembers();
        return ResponseEntity.ok(members);
    }
}
