package roomescape.controller.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.response.MembersResponse;
import roomescape.service.MemberService;
import roomescape.service.dto.output.MemberOutput;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<MembersResponse> getAllMembers() {
        final List<MemberOutput> outputs = memberService.getAllMembers();
        return ResponseEntity.ok(MembersResponse.from(outputs));
    }
}
