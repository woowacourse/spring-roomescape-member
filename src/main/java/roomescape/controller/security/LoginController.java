package roomescape.controller.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.Member;
import roomescape.infrastructure.TokenExtractor;
import roomescape.service.AuthService;
import roomescape.service.dto.login.LoginRequest;
import roomescape.service.dto.login.MemberNameResponse;

import java.util.Date;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest) {
        ResponseCookie cookie = ResponseCookie.from("token", authService.createToken(loginRequest))
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberNameResponse> checkLogin(HttpServletRequest request) {
        String token = TokenExtractor.extract(request);
        Member member = authService.findMemberByToken(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.DATE, new Date().toString())
                .body(MemberNameResponse.of(member));
    }
}
