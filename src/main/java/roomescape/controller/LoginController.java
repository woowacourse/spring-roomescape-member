package roomescape.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.service.LoginService;
import roomescape.service.dto.login.LoginCheckResponse;
import roomescape.service.dto.login.LoginRequest;
import roomescape.service.dto.login.LoginResponse;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String readLoginPage() {
        return "/login";
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        LoginResponse data = loginService.login(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, "token=" + data.accessToken())
                .build();
    }

    @ResponseBody
    @GetMapping("/check")
    public LoginCheckResponse checkLogin(@CookieValue("token") String token) {
        return loginService.checkLogin(token);
    }
}
