package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.service.MemberService;
import roomescape.util.CookieUtil;
import roomescape.util.TokenUtil;

@RestController
public class AuthController {
    private final MemberService memberService;
    private final TokenUtil tokenUtil;
    private final CookieUtil cookieUtil;

    public AuthController(final MemberService memberService, final TokenUtil tokenUtil, final CookieUtil cookieUtil) {
        this.memberService = memberService;
        this.tokenUtil = tokenUtil;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        Member member = memberService.findMemberWithEmailAndPassword(request);
        String token = tokenUtil.makeToken(member);
        CookieUtil.add(response, token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        cookieUtil.addExpiredCookie(request, response);

        return ResponseEntity.ok().build();
    }

    @GetMapping("login/check")
    public ResponseEntity<LoginCheckResponse> checkMember(HttpServletRequest request) {
        LoginCheckResponse loginCheckResponse = memberService.checkMember(request);
        return ResponseEntity.ok().body(loginCheckResponse);
    }
}
