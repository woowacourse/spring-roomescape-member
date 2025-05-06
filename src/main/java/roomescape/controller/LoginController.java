package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;
    private final AuthService authService;

    public LoginController(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage() {
        return "login";
    }

    @PostMapping
    public ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse response
    ) {
        Member member = memberService.findByEmailAndPassword(request);
        String accessToken = authService.createTokenByMember(member);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
