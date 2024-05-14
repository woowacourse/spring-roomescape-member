package roomescape.member.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.member.dto.MemberRequestDto;
import roomescape.member.dto.MemberResponseDto;
import roomescape.member.service.MemberService;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> findAll() {
        List<MemberResponseDto> memberResponseDtos = memberService.findAll().stream()
                .map(MemberResponseDto::new)
                .toList();
        return ResponseEntity.ok(memberResponseDtos);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> create(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        long id = memberService.save(memberRequestDto);
        return ResponseEntity.created(URI.create("/signup/" + id)).build();
    }

}
