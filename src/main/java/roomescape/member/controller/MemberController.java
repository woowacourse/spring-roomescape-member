package roomescape.member.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.service.MemberService;

@RestController
@RequestMapping(value = "/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping()
    public ResponseEntity<List<MemberResponse>> findAll() {
        List<MemberResponse> all = memberService.findAll();
        return ResponseEntity.ok(all);
    }

}
