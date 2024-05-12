package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.repository.MemberRepository;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<List<Member>> getMembers() {
        return ResponseEntity.ok().body(memberRepository.findAll());
    }
}
