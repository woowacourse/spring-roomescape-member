package roomescape.controller.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.domain.Email;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.TokenRequest;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@Controller
@RequestMapping("/login")
public class LoginPageController {

    private final AuthService authService;
    private final MemberService memberService;

    public LoginPageController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @GetMapping
    public String loginPage() {
        return "login";
    }

    @PostMapping
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        boolean validated = memberService.validateMember(loginRequest);
        if (validated) {
            TokenRequest tokenRequest = new TokenRequest(new Email(loginRequest.email()));
            Cookie cookie = new Cookie("token", authService.createToken(tokenRequest).token());
            response.addCookie(cookie);
        }
        // TODO 로그인 안되었을 때
    }
}
