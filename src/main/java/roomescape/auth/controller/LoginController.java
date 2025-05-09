package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.controller.dto.LoginRequest;
import roomescape.auth.controller.dto.MemberNameResponse;
import roomescape.auth.service.AuthService;
import roomescape.auth.service.dto.CreateTokenServiceRequest;
import roomescape.member.domain.Member;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void tokenLogin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        CreateTokenServiceRequest request = CreateTokenServiceRequest.from(loginRequest);

        String token = authService.createToken(request);
        ResponseCookie cookie = authService.createCookie(token);

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @GetMapping("/login/check")
    public MemberNameResponse checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = authService.extractTokenFromCookie(cookies);
        Member member = authService.findMember(token);

        return MemberNameResponse.from(member);
    }
}
