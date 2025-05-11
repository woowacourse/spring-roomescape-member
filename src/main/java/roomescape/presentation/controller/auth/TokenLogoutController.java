package roomescape.presentation.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenLogoutController {

    @PostMapping("/logout")
    public ResponseEntity<Void> login() {
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .header("Location", "/")
                // https://stackoverflow.com/a/5285982
                .header("Set-Cookie", "token=deleted; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT")
                .build();
    }
}
