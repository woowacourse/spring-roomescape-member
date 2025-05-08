package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequestDto;
import roomescape.dto.MemberResponseDto;
import roomescape.dto.RegistrationRequestDto;
import roomescape.dto.TokenResponseDto;
import roomescape.service.LoginService;
import roomescape.service.SignupService;
import roomescape.util.CookieTokenExtractor;

@RestController
public class AuthController {

    private final SignupService signupService;
    private final LoginService loginService;
    private final CookieTokenExtractor authorizationExtractor;

    public AuthController(SignupService signupService, LoginService loginService) {
        this.signupService = signupService;
        this.loginService = loginService;
        this.authorizationExtractor = new CookieTokenExtractor();
    }

    @PostMapping("/members")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody RegistrationRequestDto registrationRequestDto) {
        signupService.signup(registrationRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> processLogin(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = loginService.createToken(loginRequestDto);

        Cookie cookie = new Cookie("token", tokenResponseDto.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponseDto> checkLogin(HttpServletRequest request) {
        String token = authorizationExtractor.extract(request);
        MemberResponseDto response = loginService.findMemberByToken(token);
        return ResponseEntity.ok().body(response);
    }
}
