package roomescape.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.domain.exception.AuthFailException;
import roomescape.dto.MemberModel;
import roomescape.dto.request.MemberFindRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.MemberService;
import roomescape.service.TokenService;

@RestController
public class AuthController {
    private final TokenService tokenService;
    private final MemberService memberService;

    public AuthController(TokenService tokenService, MemberService memberService) {
        this.tokenService = tokenService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberFindRequest request, HttpServletResponse response) {
        MemberModel member = memberService.readMember(request);
        TokenResponse tokenResponse = tokenService.createToken(member);

        Cookie cookie = new Cookie("token", tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        Long id = tokenService.findTokenId(request.getCookies());
        MemberResponse response = memberService.readMember(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout();
        } catch (ServletException e) {
            throw new AuthFailException("로그아웃이 실패하였습니다.");
        }

        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
