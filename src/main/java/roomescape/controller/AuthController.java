package roomescape.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginInfo;
import roomescape.dto.request.AuthRequest;
import roomescape.resolver.LoginRequired;
import roomescape.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public void login(@Valid @RequestBody final AuthRequest request) {
        authService.login(request);
    }

    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }

    @GetMapping("/login/check")
    public LoginInfo checkLogin(@LoginRequired final LoginInfo loginInfo) {
        return loginInfo;
    }
}
