package roomescape.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.MemberRequestDto;
import roomescape.dto.MemberResponseDto;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.service.MemberService;
import roomescape.token.CookieGenerator;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public MemberResponseDto saveMember(@RequestBody MemberRequestDto memberRequestDto) {
        return memberService.saveMember(memberRequestDto);
    }

    @PostMapping("/login")
    public void login(@RequestBody TokenRequest tokenRequest,
        HttpServletResponse httpServletResponse) {
        TokenResponse tokenResponse = memberService.requestLogin(tokenRequest);
        httpServletResponse.addCookie(CookieGenerator.generate(tokenResponse.token()));
    }
}
