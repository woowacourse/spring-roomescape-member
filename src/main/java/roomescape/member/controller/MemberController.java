package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.member.dto.LoginCheckResponse;
import roomescape.member.dto.LoginRequest;
import roomescape.member.service.MemberService;

@Controller
public class MemberController {
    private static final String TOKEN = "token";
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest,
                                      HttpServletResponse httpServletResponse) {
        String token = memberService.checkLogin(loginRequest);
        httpServletResponse.addCookie(new Cookie(TOKEN, token));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(@CookieValue("token") String token) {
       LoginCheckResponse loginCheckResponse = memberService.findMemberByToken(token);
       return ResponseEntity.ok(loginCheckResponse);
    }
}
