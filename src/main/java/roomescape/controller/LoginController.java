package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.configuration.AuthenticationPrincipal;
import roomescape.dto.LoginMember;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberResponse;
import roomescape.infrastructure.AuthorizationExtractor;
import roomescape.service.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest,
                                      HttpServletResponse response) {
        Cookie cookie = new Cookie("token", authService.createToken(loginRequest));
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> findMemberInformation(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(MemberResponse.from(loginMember));
    }
}
