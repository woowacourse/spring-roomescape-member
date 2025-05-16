package roomescape.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.MemberRequest;
import roomescape.model.user.Member;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<Member>> getUsers() {
        return ResponseEntity.ok().body(memberService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<Member> addMember(@RequestBody MemberRequest memberRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.addMember(memberRequest));
    }
}
