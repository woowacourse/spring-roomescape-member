package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.LoginMember;
import roomescape.dto.member.LoginMemberResponse;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.RegistrationRequest;
import roomescape.dto.member.TokenResponse;
import roomescape.exception.UnauthorizedAccessException;
import roomescape.service.LoginMemberService;
import roomescape.service.LoginService;
import roomescape.service.SignupService;
import roomescape.util.CookieTokenExtractor;

@RestController
public class AuthController {

    private final SignupService signupService;
    private final LoginService loginService;
    private final LoginMemberService loginMemberService;
    private final CookieTokenExtractor authorizationExtractor;

    public AuthController(SignupService signupService, LoginService loginService, LoginMemberService loginMemberService) {
        this.signupService = signupService;
        this.loginService = loginService;
        this.loginMemberService = loginMemberService;
        this.authorizationExtractor = new CookieTokenExtractor();
    }

    @PostMapping("/members")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody RegistrationRequest registrationRequest) {
        signupService.signup(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<LoginMemberResponse>> getAllMembers(LoginMember member) {
        if (member.getRole().equalsIgnoreCase("USER")) {
            throw new UnauthorizedAccessException("[ERROR] 접근 권한이 없습니다.");
        }

        List<LoginMemberResponse> response = loginMemberService.findAllMembers()
                .stream()
                .map(value -> new LoginMemberResponse(value.getId(), value.getName()))
                .toList();
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> processLogin(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        TokenResponse tokenResponse = loginService.createToken(loginRequest);

        Cookie cookie = new Cookie("token", tokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginMemberResponse> checkLogin(@CookieValue(name = "token", required = false) HttpServletRequest request) {
        String token = authorizationExtractor.extract(request);
        LoginMemberResponse response = loginService.findMemberByToken(token);
        return ResponseEntity.ok().body(response);
    }
}
