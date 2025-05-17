package roomescape.member.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import roomescape.member.auth.controller.dto.LoginRequest;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.service.MemberService;

import java.util.Arrays;
import java.util.Optional;

@RestController
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/login/check")
    public MemberResponse loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies)
                .orElseThrow(() -> new IllegalArgumentException("token 쿠키가 없습니다."));
        return memberService.search(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String accessToken = memberService.login(request);
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        extractTokenFromCookie(cookies).ifPresent(token -> {
            Cookie expiredCookie = new Cookie("token", null);
            expiredCookie.setMaxAge(0);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setPath("/");
            response.addCookie(expiredCookie);
        });
    }

    private Optional<String> extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> "token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }

}
