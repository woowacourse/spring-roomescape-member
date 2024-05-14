package roomescape.controller.api;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getAllMembers() {
        List<MemberResponse> responses = memberService.getAllMembers();

        return ResponseEntity.ok()
                .body(responses);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> addMember(@RequestBody MemberRequest request) {
        MemberResponse response = memberService.addMember(request);
        URI location = URI.create("/member/" + response.id());

        return ResponseEntity.created(location)
                .body(response);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        memberService.deleteMemberById(id);

        return ResponseEntity.noContent()
                .build();
    }
}
