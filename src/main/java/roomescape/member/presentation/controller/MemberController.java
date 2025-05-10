package roomescape.member.presentation.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.application.dto.GetMemberResponse;
import roomescape.member.application.service.MemberService;
import roomescape.member.presentation.dto.MemberNameResponse;
import roomescape.member.presentation.dto.RegisterRequest;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberNameResponse> register(@RequestBody RegisterRequest registerRequest) {
        MemberNameResponse response = memberService.signup(registerRequest);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<GetMemberResponse>> getMembers() {
        List<GetMemberResponse> members = memberService.getMembers();
        return ResponseEntity.ok().body(members);
    }
}
