package roomescape.web.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.MemberService;
import roomescape.service.response.MemberResponse;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> response = memberService.getAllMembers();

        return ResponseEntity.ok(response);
    }
}
