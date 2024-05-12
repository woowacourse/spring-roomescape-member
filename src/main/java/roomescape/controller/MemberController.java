package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.response.MemberResponse;
import roomescape.repository.MemberRepository;
import roomescape.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        return ResponseEntity.ok().body(memberService.findAll());
    }
}
