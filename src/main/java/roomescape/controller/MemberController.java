package roomescape.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.MemberService;
import roomescape.service.dto.request.MemberRequest;
import roomescape.service.dto.response.MemberResponse;

@RestController()
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberRequest memberRequest) {
        return ResponseEntity.ok(memberService.createMember(memberRequest));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }
}
