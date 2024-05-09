package roomescape.controller.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.infrastructure.CookieGenerator;
import roomescape.infrastructure.auth.Token;
import roomescape.service.serviceimpl.LoginService;
import roomescape.service.serviceimpl.MemberService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final CookieGenerator cookieGenerator;
    private final LoginService loginService;
    private final MemberService memberService;

    public LoginController(
            final CookieGenerator cookieGenerator,
            final LoginService loginService,
            final MemberService memberService
    ) {
        this.cookieGenerator = cookieGenerator;
        this.loginService = loginService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        Token token = loginService.login(
                memberService.findMemberByEmailAndPassword(request.email(), request.password()),
                request.email(),
                request.password()
        );
        response.addCookie(cookieGenerator.generate(token));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Token token = cookieGenerator.getToken(request.getCookies());
        Long memberId = loginService.findMemberIdByToken(token);
        MemberResponse memberResponse = memberService.findMemberById(memberId);
        return ResponseEntity.ok(memberResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        cookieGenerator.resetCookie(request.getCookies());
        return ResponseEntity.ok().build();
    }
}
