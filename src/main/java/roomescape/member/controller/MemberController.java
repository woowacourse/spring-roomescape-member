package roomescape.member.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.request.SignupRequest;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.dto.response.SignupResponse;
import roomescape.member.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findAllUsers() {
        return ResponseEntity.ok(memberService.findAllUsers());
    }

    @PostMapping
    public ResponseEntity<SignupResponse> signUp(final @RequestBody SignupRequest signupRequest) {
        SignupResponse response = memberService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
