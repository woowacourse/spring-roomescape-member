package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.resolver.AuthenticationPrincipal;
import roomescape.service.MemberService;
import roomescape.service.dto.CreateMemberRequest;
import roomescape.service.dto.LoginMember;
import roomescape.service.dto.LoginMemberRequest;
import roomescape.service.dto.MemberResponse;

@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/members/signup")
    public void signup(@Valid @RequestBody CreateMemberRequest requestDto) {
        memberService.signup(requestDto);
    }

    @PostMapping("/members/login")
    public void login(@Valid @RequestBody LoginMemberRequest requestDto, HttpServletResponse response) {
        String token = memberService.login(requestDto);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(300);
        response.addCookie(cookie);
    }

    @GetMapping("/members/login/check")
    public MemberResponse checkLogin(@AuthenticationPrincipal LoginMember loginMember) {
        return new MemberResponse(loginMember);
    }

    @PostMapping("/members/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/admin/members")
    public List<MemberResponse> findAllMembers() {
        return memberService.findAllMemberNames();
    }
}
