package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.dto.MemberResponse;
import roomescape.service.MemberService;

@Controller
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> responses = memberService.findAll();

        return ResponseEntity.ok(responses);
    }
}
