package roomescape.controller.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.dto.MemberCheckResponse;
import roomescape.service.MemberService;

@Controller
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberCheckResponse>> getAll() {
        List<MemberCheckResponse> responses = memberService.findAll();

        return ResponseEntity.ok(responses);
    }
}
