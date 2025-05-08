package roomescape.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.MemberSignUpRequest;
import roomescape.dto.response.MemberSignUpResponse;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberSignUpResponse> signup(@RequestBody @Valid MemberSignUpRequest request) {
        MemberSignUpResponse response = memberService.signup(request);
        return ResponseEntity.ok(response);
    }
}
