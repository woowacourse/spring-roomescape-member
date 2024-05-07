package roomescape.member.controller;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.MemberSignUpRequest;
import roomescape.member.service.MemberSignUpService;

@RestController
public class MemberSignUpApiController {

    private final MemberSignUpService memberSignUpService;

    public MemberSignUpApiController(MemberSignUpService memberSignUpService) {
        this.memberSignUpService = memberSignUpService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberSignUpRequest memberSignUpRequest) {
        Long memberId = memberSignUpService.save(memberSignUpRequest);

        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }
}
