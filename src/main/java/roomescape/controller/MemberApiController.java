package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.resolver.AuthenticationPrincipal;
import roomescape.domain.LoginMember;
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
        String token = memberService.login(requestDto);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60);
        response.addCookie(cookie);
    }

    @GetMapping("/members/login/check")
    public MemberResponseDto checkLogin(@AuthenticationPrincipal LoginMember loginMember) {
        return new MemberResponseDto(loginMember);
    }

    @PostMapping("/members/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @GetMapping("/members")
    public List<MemberResponseDto> findAllMembers() {
        return memberService.findAllMemberNames();
    }
}
