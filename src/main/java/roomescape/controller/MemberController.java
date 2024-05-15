package roomescape.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.member.MemberCreateRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> readMembers() {
        return ResponseEntity.ok().body(memberService.findAll());
    }

    @PostMapping
    public ResponseEntity<MemberResponse> registerMember(@RequestBody @Valid MemberCreateRequest request) {
        MemberResponse memberResponse = memberService.add(request);
        return ResponseEntity.created(URI.create("/members/" + memberResponse.id())).body(memberResponse);
    }
}
