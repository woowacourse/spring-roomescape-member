package roomescape.controller.member;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.domain.member.MemberInfo;
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
}
