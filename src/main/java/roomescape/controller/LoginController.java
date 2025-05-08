package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.login.TokenRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.service.login.JwtLoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private JwtLoginService jwtLoginService;

    public LoginController(JwtLoginService jwtLoginService) {
        this.jwtLoginService = jwtLoginService;
    }

    @PostMapping
    public void userLogin(@RequestBody TokenRequest tokenRequest, HttpServletResponse httpServletResponse) {
        String jwtToken = jwtLoginService.createToken(tokenRequest);
        Cookie cookie = new Cookie("token", jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        httpServletResponse.setStatus(200);
    }

    @GetMapping("check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        MemberResponse memberResponse = jwtLoginService.findMemberByToken(cookies);
        return ResponseEntity.ok().body(memberResponse);
    }
}
