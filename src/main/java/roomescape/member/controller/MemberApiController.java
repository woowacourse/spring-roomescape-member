package roomescape.member.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.controller.request.SignUpRequest;
import roomescape.member.controller.response.MemberResponse;
import roomescape.member.service.MemberService;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> responses = memberService.getMembers();

        return ResponseEntity.ok(responses);
    }
}
