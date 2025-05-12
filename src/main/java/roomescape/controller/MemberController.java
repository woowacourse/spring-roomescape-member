package roomescape.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.signup.SignupRequest;
import roomescape.service.member.MemberServiceImpl;

@RestController
@RequestMapping("members")
public class MemberController {

    private final MemberServiceImpl memberServiceImpl;

    public MemberController(MemberServiceImpl memberServiceImpl) {
        this.memberServiceImpl = memberServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMember() {
        return ResponseEntity.ok().body(memberServiceImpl.findAllMembers());
    }

    @PostMapping
    public ResponseEntity<MemberResponse> addMember(@RequestBody SignupRequest signupRequest){
        return ResponseEntity.created(URI.create("/members")).body(memberServiceImpl.addMember(signupRequest));
    }
}
