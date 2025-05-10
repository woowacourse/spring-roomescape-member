package roomescape.controller.admin;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.dto.request.MemberCreationRequest;
import roomescape.dto.response.MemberProfileResponse;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberProfileResponse> getAllMembers() {
        List<Member> members = memberService.getAllByRole(MemberRole.GENERAL);
        return members.stream()
                .map(MemberProfileResponse::new)
                .toList();
    }

    @PostMapping
    public ResponseEntity<MemberProfileResponse> addMember(
            @Valid @RequestBody MemberCreationRequest request
    ) {
        long memberId = memberService.addMember(request);
        Member member = memberService.getById(memberId);
        return ResponseEntity.created(URI.create("/member/" + memberId))
                .body(new MemberProfileResponse(member));
    }
}
