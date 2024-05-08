package roomescape.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.MemberLoginRequest;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberLoginRequest memberLoginRequest) {
        String issuedToken = memberService.login(memberLoginRequest);

        return ResponseEntity.ok()
                .header(SET_COOKIE, issuedToken)
                .build();
    }
}
