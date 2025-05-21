package roomescape.controller.member;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.member.request.MemberRegisterRequest;
import roomescape.dto.member.response.MemberRegisterResponse;
import roomescape.dto.member.response.MemberResponse;
import roomescape.service.member.MemberService;

@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        final List<MemberResponse> response = memberService.findAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MemberRegisterResponse> register(@RequestBody @Valid final MemberRegisterRequest request) {
        final MemberRegisterResponse response = memberService.register(request);
        return ResponseEntity.created(URI.create("/members/" + response.id())).body(response);
    }

}
