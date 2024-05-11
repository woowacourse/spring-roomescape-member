package roomescape.member.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.service.dto.SignUpRequest;
import roomescape.member.service.MemberResponse;
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

    @GetMapping
    public List<MemberResponse> findAllMembers() {
        return memberService.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid SignUpRequest signUpRequest) {
        MemberResponse memberResponse = memberService.create(signUpRequest);
        return ResponseEntity.created(URI.create("/members/"+memberResponse.id())).build();
    }
}
