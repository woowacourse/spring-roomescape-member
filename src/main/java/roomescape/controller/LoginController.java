package roomescape.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.util.CookieUtil;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;
    private final AuthService authService;

    public LoginController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        Member member = memberService.findByEmailAndPassword(request);
        String accessToken = authService.createTokenByMember(member);

        CookieUtil.addCookie("token", accessToken, response);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkAuthentication(Member member) {
        MemberResponse response = MemberResponse.from(member);

        return ResponseEntity.ok(response);
    }
}
