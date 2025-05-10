package roomescape.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import roomescape.auth.RequestMember;
import roomescape.domain.Member;
import roomescape.dto.SignupRequest;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = memberService.login(request);
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // TODO: members 독스 추가
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/members")
    public List<MemberResponse> members() {
        return memberService.getAll();
    }

    // TODO: 비로그인 상태인 경우 UserId(required=false)와 같이 변경(현재는 비로그인 상태 시 400 에러 발생: [ERROR] JWT String argument cannot be null or empty.)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/login/check")
    public LoginCheckResponse loginCheck(@RequestMember Member member) {
        return memberService.loginCheck(member);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequest request) {
        memberService.signup(request);
    }
}
