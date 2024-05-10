package roomescape.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.Member;
import roomescape.dto.LogInRequest;
import roomescape.dto.MemberNameResponse;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LogInRequest logInRequest) {
        String token = "token=" + memberService.logIn(logInRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, token)
                .build();
    }

    @GetMapping("/check") //todo: URI 레스트풀하게 변경
    public ResponseEntity<MemberNameResponse> loginCheck(Member member) {
        MemberNameResponse name = new MemberNameResponse(member.getName());

        return ResponseEntity.ok()
                .body(name);
    }
}
