package roomescape.controller.login;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody final LoginRequest request) {
        return ResponseEntity.ok().build();
    }
}
