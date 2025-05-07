package roomescape.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.user.controller.request.LoginRequest;
import roomescape.user.domain.Member;
import roomescape.user.repository.MemberRepository;

@RestController
public class MemberApiController {

    private final MemberRepository memberRepository;

    public MemberApiController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {

        Member member = Member.create("name", request.email(), request.password());
        Member saved = memberRepository.save(member);

        return ResponseEntity.ok().build();
    }
}
