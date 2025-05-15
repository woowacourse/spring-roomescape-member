package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.controller.dto.SignupRequest;
import roomescape.auth.controller.dto.SignupResponse;
import roomescape.auth.controller.dto.TokenRequest;
import roomescape.auth.service.AuthService;
import roomescape.common.util.CookieUtil;
import roomescape.controller.dto.member.MemberResponse;
import roomescape.entity.Member;

@RestController
public class AuthApiController {

    private final AuthService authService;

    public AuthApiController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/members")
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest signupRequest) {
        Member member = authService.register(
                signupRequest.name(),
                signupRequest.email(),
                signupRequest.password()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(SignupResponse.from(member));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid TokenRequest tokenRequest, HttpServletResponse response) {
        String jwtToken = authService.login(tokenRequest.email(), tokenRequest.password());
        Cookie cookie = CookieUtil.addTokenCookie(jwtToken);
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> checkLogin(HttpServletRequest request) {
        String token = CookieUtil.extractTokenFromCookie(request.getCookies());
        Member member = authService.findMemberByToken(token);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie deleteCookie = CookieUtil.deleteTokenCookie();
        response.addCookie(deleteCookie);
        return ResponseEntity.ok().build();
    }
}
