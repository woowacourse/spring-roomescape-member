package roomescape.member;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.login.domain.Member;
import roomescape.login.dto.MemberResponse;
import roomescape.login.service.MemberService;

@RequestMapping("/members")
@RequiredArgsConstructor
@RestController
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getMembers() {
        final List<Member> members = memberService.findAll();
        final List<MemberResponse> memberResponses = members.stream()
                .map(MemberResponse::from)
                .toList();

        return ResponseEntity.ok(memberResponses);
    }
}
