package roomescape.login.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import javax.naming.AuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.login.dto.LoginRequest;
import roomescape.login.service.LoginService;
import roomescape.member.dto.MemberNameResponse;

@RestController
@RequestMapping("/login")
public class LoginController {

    private static final String TOKEN = "token";
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response)
            throws AuthenticationException {
        String token = loginService.createLoginToken(loginRequest);

        Cookie cookie = new Cookie(TOKEN, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/check")
    public MemberNameResponse getLoginMemberName(MemberNameResponse memberNameResponse) {
        return memberNameResponse;
    }
}
