package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberProfileResponse;
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
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        Member member = memberService.findMember(loginRequest);
        Cookie cookie = authService.generateCookie(member);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberProfileResponse> checkMemberProfile(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        MemberProfileResponse memberResponse = authService.findMemberProfile(cookies);
        return ResponseEntity.ok().body(memberResponse);
    }
}
