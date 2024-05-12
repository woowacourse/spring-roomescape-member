package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import roomescape.member.AuthService;
import roomescape.member.request.LoginRequest;
import roomescape.member.response.MemberResponse;
import roomescape.member.service.MemberService;

@RequestMapping("/login")
@RestController
public class LoginController {

    private final AuthService authService;
    private final MemberService memberService;
    public LoginController(AuthService authService,MemberService memberService) {
        this.authService = authService;
        this.memberService=memberService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.createToken(request);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = authService.extractTokenFromCookie(cookies);
        long id = authService.findMemberIdByToken(token);
        MemberResponse response = new MemberResponse(memberService.findMemberNameById(id));
        return ResponseEntity.ok().body(response);
    }
}
