package roomescape.controller.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.annotation.AuthenticationPrinciple;
import roomescape.dto.other.AuthenticationInformation;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberNameResponse;
import roomescape.service.AuthenticationService;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        String accessToken = authenticationService.login(request.email(), request.password());
        ResponseCookie cookie = ResponseCookie.from("access", accessToken)
                .path("/")
                .httpOnly(true)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/login/check")
    public MemberNameResponse checkLogin(
            @AuthenticationPrinciple AuthenticationInformation authenticationInformation
    ) {
        return new MemberNameResponse(authenticationInformation.name());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from("access", null)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
}
