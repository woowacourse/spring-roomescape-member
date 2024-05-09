package roomescape.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.api.dto.request.MemberCreateRequest;
import roomescape.service.MemberService;
import roomescape.service.dto.output.MemberCreateOutput;

import java.net.URI;

@RestController
@RequestMapping("member")
public class MemberApiController {
    private final MemberService memberService;

    public MemberApiController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody final MemberCreateRequest request) {
        final MemberCreateOutput output = memberService.createMember(request.toInput());
        return ResponseEntity.created(URI.create("/reservations/" + output.id()))
                .build();
    }
}
