package roomescape.presentation;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.MemberService;
import roomescape.presentation.dto.MemberRequestDto;

@RestController
public final class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        Long memberId = memberService.registerMember(memberRequestDto);
        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }
}
