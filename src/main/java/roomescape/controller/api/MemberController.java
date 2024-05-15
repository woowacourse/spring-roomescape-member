package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

import java.util.List;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> members() {
        List<MemberResponse> memberResponses = memberService.findAllMembers()
                .stream()
                .map(MemberResponse::new)
                .toList();
        return ResponseEntity.ok(memberResponses);
    }
}
