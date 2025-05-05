package roomescape.controller.authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.dto.member.LoginRequestDto;
import roomescape.dto.member.LoginResponseDto;
import roomescape.dto.member.MemberResponseDto;
import roomescape.dto.member.SignupRequestDto;
import roomescape.service.member.MemberService;

@RestController
public class AuthenticationController {

    private final MemberService memberService;

    public AuthenticationController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                                                  HttpServletResponse response) {
        String token = memberService.login(loginRequestDto);
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponseDto> loginCheck(@CookieValue("token") String token) {
        Member member = memberService.getMemberByToken(token);
        MemberResponseDto memberResponseDto = new MemberResponseDto(member.getName());
        return ResponseEntity.ok(memberResponseDto);
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {

        memberService.signup(signupRequestDto);
        return ResponseEntity.created(URI.create("/login/check")).build();
    }
}
