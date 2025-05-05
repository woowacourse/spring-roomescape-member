package roomescape.controller.member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.member.LoginRequestDto;
import roomescape.dto.member.LoginResponseDto;
import roomescape.dto.member.SignupRequestDto;
import roomescape.service.member.MemberService;

@RestController
public class LoginController {

    private final MemberService memberService;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto,
                                                  HttpServletResponse response) {
        String token = memberService.login(loginRequestDto);
        response.addCookie(new Cookie("token", token));

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        memberService.signup(signupRequestDto);
        return ResponseEntity.created(URI.create("/login/check")).build();
    }
}
