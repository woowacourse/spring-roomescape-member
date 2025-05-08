package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.CheckResponseDto;
import roomescape.dto.MemberRequestDto;
import roomescape.dto.MemberResponseDto;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.service.MemberService;
import roomescape.token.Cookies;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/members")
    public MemberResponseDto saveMember(@RequestBody MemberRequestDto memberRequestDto) {
        return memberService.saveMember(memberRequestDto);
    }

    @PostMapping("/login")
    public void login(@RequestBody TokenRequest tokenRequest,
        HttpServletResponse httpServletResponse) {
        TokenResponse tokenResponse = memberService.requestLogin(tokenRequest);
        httpServletResponse.addCookie(Cookies.generate(tokenResponse.token()));
    }

    @GetMapping("/login/check")
    public CheckResponseDto checkLogin(HttpServletRequest request) {
        String token = Cookies.get(request.getCookies());
        return memberService.validate(token);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse httpServletResponse) {
        String token = Cookies.get(request.getCookies());
        Cookie jwtCookie = new Cookie("token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        httpServletResponse.addCookie(jwtCookie);
    }
}
