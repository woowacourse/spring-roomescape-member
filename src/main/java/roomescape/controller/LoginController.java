package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.annotation.AuthenticationPrincipal;
import roomescape.domain.Member;
import roomescape.dto.LoginRequestDto;
import roomescape.dto.LoginResponseDto;
import roomescape.service.AuthenticationService;
import roomescape.service.MemberService;

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
        memberService.login(loginRequestDto.email(), loginRequestDto.password());
        String token = authenticationService.createToken(loginRequestDto.email());
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("login/check")
    public ResponseEntity<LoginResponseDto> checkLogin(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok().body(new LoginResponseDto(member.getName()));
    }

    //TODO: logout, register 구현

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
