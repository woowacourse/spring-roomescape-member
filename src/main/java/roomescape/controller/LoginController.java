package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.dto.member.LoginMemberResponse;
import roomescape.dto.member.MemberLoginRequest;
import roomescape.service.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid MemberLoginRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", authService.createToken(request));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<LoginMemberResponse> check(Member member) {
        return ResponseEntity.ok(LoginMemberResponse.from(member.getName()));
    }
}
