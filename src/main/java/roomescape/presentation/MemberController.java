package roomescape.presentation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.AuthenticationService;
import roomescape.business.LoginInformation;
import roomescape.presentation.dto.LoginRequestDto;
import roomescape.presentation.dto.MemberCheckResponseDto;

@RestController
public class MemberController {

    private final AuthenticationService authenticationService;

    @Autowired
    public MemberController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        String token = authenticationService.login(loginRequest);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberCheckResponseDto> loginCheck(LoginInformation loginInformation) {
        return ResponseEntity.ok()
                .body(new MemberCheckResponseDto(loginInformation.name()));
    }
}
