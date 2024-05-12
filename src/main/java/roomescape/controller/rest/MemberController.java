package roomescape.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.member.MemberResponse;
import roomescape.service.MemberService;

import java.util.List;

// TODO: 테스트 추가
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> readMembers() {
        return ResponseEntity.ok(memberService.findAllMembers());
    }
}
