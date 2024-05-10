package roomescape.web.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.core.domain.Member;
import roomescape.core.dto.MemberResponseDto;
import roomescape.core.dto.SignUpRequestDto;
import roomescape.core.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody @Valid final SignUpRequestDto request) {
        final Member member = memberService.create(request);
        final MemberResponseDto response = new MemberResponseDto(member);
        return ResponseEntity.created(URI.create("/members/" + response.getId()))
                .body(response);
    }
}

