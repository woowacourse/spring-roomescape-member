package roomescape.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.MemberResponse;
import roomescape.member.service.MemberService;

import java.util.List;

@RestController
@RequestMapping(("/members"))
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> responses = memberService.findAll();

        return ResponseEntity.ok(responses);
    }
}
