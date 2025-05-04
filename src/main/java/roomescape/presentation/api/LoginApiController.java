package roomescape.presentation.api;

import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.model.vo.Authentication;
import roomescape.business.service.AuthService;
import roomescape.presentation.dto.request.LoginRequest;

@RestController
public class LoginApiController {

    private final AuthService authService;

    public LoginApiController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request) {
        Authentication authentication = authService.authenticate(request.email(), request.password());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, authentication.token());
        return ResponseEntity.noContent().headers(headers).build();
    }
}
