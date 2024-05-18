package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.domain.Member;
import roomescape.dto.request.MemberCreateRequest;
import roomescape.dto.response.MemberLoginResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.LoginService;

@Controller
@RequestMapping("/login")
public class LoginController {

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String showMemberLoginPage() {
        return "login";
    }

    @PostMapping
    public ResponseEntity<TokenResponse> login(HttpServletResponse response,
                                               @RequestBody MemberCreateRequest memberCreateRequest) {
        TokenResponse tokenResponse = loginService.createToken(memberCreateRequest);
        Cookie cookie = new Cookie("token", tokenResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/check")
    public ResponseEntity<MemberLoginResponse> checkLogin(Member member) {
        return ResponseEntity.ok().body(MemberLoginResponse.from(member));
    }
}
