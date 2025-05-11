package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.service.MemberService;
import roomescape.util.CookieUtil;
import roomescape.util.TokenUtil;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final MemberService memberService;
    private final TokenUtil tokenUtil;

    public LoginController(final MemberService memberService, final TokenUtil tokenUtil) {
        this.memberService = memberService;
        this.tokenUtil = tokenUtil;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Member member = memberService.findMemberWithEmailAndPassword(request);
        String token = tokenUtil.makeToken(member);
        CookieUtil.add(response, token);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> checkMember(HttpServletRequest request) {
        LoginCheckResponse loginCheckResponse = memberService.checkMember(request);
        return ResponseEntity.ok().body(loginCheckResponse);
    }
}
