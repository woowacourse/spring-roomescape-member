package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.dto.LoginRequestDto;
import roomescape.dto.LoginResponseDto;
import roomescape.service.AuthenticationService;
import roomescape.service.MemberService;

import java.util.Arrays;

@Controller
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationService authenticationService;

    public MemberController(MemberService memberService, AuthenticationService authenticationService) {
        this.memberService = memberService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        memberService.login(loginRequestDto.email(), loginRequestDto.password());
        String token = authenticationService.createToken(loginRequestDto.email());
        Cookie cookie = authenticationService.createCookie(token);
        response.addCookie(cookie);
    }

    @GetMapping("login/check")
    public ResponseEntity<LoginResponseDto> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = Arrays.stream(cookies).filter(c -> c.getName().equals("token")).findFirst().orElseThrow(() -> new IllegalStateException("토큰이 존재하지 않습니다."));
        String token = cookie.getValue();

        String email = authenticationService.getPayload(token);
        String name = memberService.findNameByEmail(email);

        return ResponseEntity.ok().body(new LoginResponseDto(name));
    }

    //TODO: logout, register 구현

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
