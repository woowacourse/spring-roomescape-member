package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.annotation.AuthenticationPrincipal;
import roomescape.domain.Member;
import roomescape.dto.LoginRequestDto;
import roomescape.dto.LoginResponseDto;
import roomescape.service.AuthenticationService;
import roomescape.service.MemberService;
import roomescape.util.CookieInterpreter;

@Controller
public class LoginController {

    private final MemberService memberService;
    private final AuthenticationService authenticationService;

    public LoginController(MemberService memberService, AuthenticationService authenticationService) {
        this.memberService = memberService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        Member member = memberService.login(loginRequestDto.email(), loginRequestDto.password());
        String token = authenticationService.createToken(member.getEmail());
        Cookie cookie = CookieInterpreter.buildCookie(token);
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponseDto> checkLogin(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new LoginResponseDto(member.getName()));
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie expiredCookie = CookieInterpreter.expireCookie(request);
        response.addCookie(expiredCookie);
    }
}
