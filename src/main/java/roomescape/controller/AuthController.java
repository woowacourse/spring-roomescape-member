package roomescape.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.LoginRequired;
import roomescape.dto.LoginInfo;
import roomescape.dto.request.AuthRequest;
import roomescape.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public void login(@Valid @RequestBody final AuthRequest request, final HttpSession session) {
        authService.login(request, session);
    }

    @PostMapping("/logout")
    public void logout(final HttpSession session) {
        authService.logout(session);
    }

    @GetMapping("/login/check")
    public LoginInfo checkLogin(@LoginRequired final LoginInfo loginInfo) {
        return loginInfo;
    }
}
