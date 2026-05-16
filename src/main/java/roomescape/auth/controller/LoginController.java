package roomescape.auth.controller;

import static roomescape.auth.LoginUser.SESSION_KEY;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginUser;
import roomescape.auth.dto.LoginRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public ResponseEntity<Void> login(
            @RequestBody LoginRequest loginRequest,
            HttpSession session
    ) {
        session.setAttribute(SESSION_KEY, new LoginUser(loginRequest.username()));
        return ResponseEntity.ok().build();
    }
}
