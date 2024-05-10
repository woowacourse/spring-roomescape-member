package roomescape.member.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.MemberSignUpRequest;
import roomescape.member.service.MemberService;
import roomescape.member.service.MemberSignUpService;

@RestController
public class MemberSignUpApiController {

    private final MemberSignUpService memberSignUpService;
    private final MemberService memberService;

    public MemberSignUpApiController(final MemberSignUpService memberSignUpService, final MemberService memberService) {
        this.memberSignUpService = memberSignUpService;
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberSignUpRequest memberSignUpRequest) {
        Long memberId = memberSignUpService.save(memberSignUpRequest);

        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> findAll() {
        List<MemberResponse> memberResponses = memberService.findAll();

        return ResponseEntity.ok(memberResponses);
    }
}
