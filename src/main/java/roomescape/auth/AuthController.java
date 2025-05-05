package roomescape.auth;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginRequest;

@RestController
public class AuthController {

    private static final String TOKEN_HEADER_FORMAT = "token=%s";

    private final AuthService authService;

    @Autowired
    public AuthController(
            final AuthService authService
    ){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody @Valid final LoginRequest request
    ) {
        final String jwt = authService.generateToken(request);
        final String tokenHeader = formatToken(jwt);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, tokenHeader)
                .build();
    }

    private String formatToken(final String jwt){
        return String.format(TOKEN_HEADER_FORMAT, jwt);
    }
}
