package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.AuthInformationResponse;
import roomescape.auth.dto.CreateTokenRequest;
import roomescape.auth.dto.CreateTokenResponse;
import roomescape.auth.service.AuthService;
import roomescape.ui.LoginMemberArgumentResolver.LoginMemberName;

@RestController
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<CreateTokenResponse> tokenLogin(@RequestBody CreateTokenRequest createTokenRequest, HttpServletResponse response) {
        CreateTokenResponse createTokenResponse = authService.createToken(createTokenRequest);
        Cookie cookie = new Cookie("token", createTokenResponse.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<AuthInformationResponse> checkLogin(@LoginMemberName String name) {
        return ResponseEntity.ok().body(new AuthInformationResponse(name));
    }
}
