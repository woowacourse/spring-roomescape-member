package roomescape.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.RoleRequired;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.SignUpRequest;
import roomescape.member.dto.SignUpResponse;
import roomescape.member.entity.Role;
import roomescape.member.service.MemberService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        SignUpResponse signUpResponse = memberService.registerUser(request);
        URI location = URI.create("/members/" + signUpResponse.getId());
        return ResponseEntity.created(location).body(signUpResponse);
    }

    @GetMapping
    @RoleRequired(Role.ADMIN)
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> memberResponses = memberService.findAll();
        return ResponseEntity.ok(memberResponses);
    }
}
