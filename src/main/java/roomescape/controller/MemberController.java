package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.MemberSignUpRequest;
import roomescape.dto.response.MemberNameSelectResponse;
import roomescape.dto.response.MemberSignUpResponse;
import roomescape.service.MemberService;

import java.util.List;

@RestController
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberNameSelectResponse>> getMembers() {
        List<MemberNameSelectResponse> memberNames = memberService.findMemberNames();
        return ResponseEntity.ok(memberNames);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberSignUpResponse> signup(@RequestBody @Valid MemberSignUpRequest request) {
        MemberSignUpResponse response = memberService.signup(request);
        return ResponseEntity.ok(response);
    }
}
