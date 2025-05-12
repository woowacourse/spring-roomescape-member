package roomescape.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.util.CookieUtil;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    @PostMapping
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        CookieUtil.expireCookie("token", response);

        return ResponseEntity.ok().build();
    }
}
