package roomescape.controller.member;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.domain.member.MemberInfo;
import roomescape.dto.member.SignupRequest;
import roomescape.service.member.MemberService;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<MemberInfo>> findAll() {
        List<MemberInfo> members = memberService.findAll();

        return ResponseEntity.ok().body(members);
    }

    @PostMapping
    public ResponseEntity<MemberInfo> createMember(@RequestBody @Valid SignupRequest signupRequest) {
        MemberInfo member = memberService.insertMember(signupRequest);

        return ResponseEntity.created(URI.create("/members/" + member.getId())).body(member);
    }
}
