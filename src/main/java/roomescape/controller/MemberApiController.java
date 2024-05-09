package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.MemberService;
import roomescape.service.dto.CreateMemberRequestDto;
import roomescape.service.dto.LoginMemberRequestDto;
import roomescape.service.dto.MemberResponseDto;

@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/members/signup")
    public void signup(@RequestBody CreateMemberRequestDto requestDto) {
        memberService.signup(requestDto);
    }

    @PostMapping("/members/login")
    public void login(@RequestBody LoginMemberRequestDto requestDto, HttpServletResponse response) {
        String token = memberService.verifyMember(requestDto);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60);
        response.addCookie(cookie);
    }

    @GetMapping("/members/login/check")
    public MemberResponseDto checkLogin(@CookieValue(name = "token", required = false) Cookie cookie) {
        if (cookie != null) {
            return memberService.verifyToken(cookie.getValue());
        }
        throw new IllegalStateException("토큰이 존재하지 않습니다.");
    }

    @PostMapping("/members/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
