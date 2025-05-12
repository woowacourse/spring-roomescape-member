package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.domain.Member;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<Member> members = memberService.findAll();
        List<MemberResponse> responses = members.stream()
                .map(MemberResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        MemberResponse response = MemberResponse.from(member);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<MemberResponse> signUp(@RequestBody @Valid SignupRequest request) {
        Member member = memberService.addMember(request);
        MemberResponse response = MemberResponse.from(member);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }
}
