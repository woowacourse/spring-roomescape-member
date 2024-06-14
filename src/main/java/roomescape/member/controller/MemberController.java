package roomescape.member.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.member.response.MemberReservationResponse;
import roomescape.member.service.MemberService;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberReservationResponse>> members() {
        List<MemberReservationResponse> members = memberService.findAll();
        return ResponseEntity.ok().body(members);
    }
}
