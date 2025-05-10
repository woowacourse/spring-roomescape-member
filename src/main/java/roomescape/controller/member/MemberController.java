package roomescape.controller.member;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.controller.member.dto.MemberResponse;
import roomescape.controller.member.dto.MemberSignupRequest;
import roomescape.service.MemberService;

@Controller
public class MemberController {

    private final MemberService service;

    public MemberController(final MemberService service) {
        this.service = service;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> addMember(@RequestBody @Valid final MemberSignupRequest request) {
        var member = service.add(request);
        return ResponseEntity.created(URI.create("/reservations/" + member.id())).body(member);
    }
}
