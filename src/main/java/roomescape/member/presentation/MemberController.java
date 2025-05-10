package roomescape.member.presentation;

import static roomescape.member.presentation.MemberController.RESERVATION_BASE_URL;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.SignupRequest;
import roomescape.member.dto.SignupResponse;
import roomescape.member.service.MemberService;

@RestController
@RequestMapping(RESERVATION_BASE_URL)
public class MemberController {

    public static final String RESERVATION_BASE_URL = "/members";
    private static final String SLASH = "/";

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        SignupResponse response = memberService.createMember(request);
        URI uri = URI.create(RESERVATION_BASE_URL + SLASH + response.id());
        return ResponseEntity.created(uri).body(response);
    }
}
