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
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@RestController
public class AuthController {
    private final MemberService memberService;
    private final AuthService authService;

    public AuthController(final MemberService memberService, final AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        Member member = memberService.findMemberWithEmailAndPassword(request);
        String token = authService.makeToken(member);
        AuthService.add(response, token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.addExpiredCookie(request, response);

        return ResponseEntity.ok().build();
    }

    @GetMapping("login/check")
    public ResponseEntity<LoginCheckResponse> checkMember(HttpServletRequest request) {
        LoginCheckResponse loginCheckResponse = memberService.checkMember(request);
        return ResponseEntity.ok().body(loginCheckResponse);
    }
}
