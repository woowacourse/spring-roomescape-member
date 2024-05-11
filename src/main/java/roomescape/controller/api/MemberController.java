package roomescape.controller.api;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.request.SignUpRequestDto;
import roomescape.dto.response.MemberResponseDto;
import roomescape.service.MemberService;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponseDto> signUp(@RequestBody @Valid final SignUpRequestDto request) {
        final Member member = memberService.create(request);
        final MemberResponseDto response = new MemberResponseDto(member);
        return ResponseEntity.created(URI.create("/members/" + response.getId()))
                .body(response);
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> findAllRoleMembers() {
        return memberService.findAllRoleMembers()
                .stream()
                .map(MemberResponseDto::new)
                .collect(collectingAndThen(toList(), ResponseEntity::ok));
    }
}

