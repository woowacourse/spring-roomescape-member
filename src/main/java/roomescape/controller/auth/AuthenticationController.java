package roomescape.controller.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.auth.LoginCheckResponse;
import roomescape.dto.auth.LoginRequest;
import roomescape.infrastructure.intercepter.AuthenticationPrincipal;
import roomescape.infrastructure.member.MemberInfo;
import roomescape.service.auth.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request,
                      HttpServletResponse response) {
        String token = authenticationService.login(request);
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.setContentType("application/json");
        response.setHeader("Keep-Alive", "timeout=60");
        response.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public LoginCheckResponse loginCheck(@AuthenticationPrincipal MemberInfo memberInfo,
                                         HttpServletResponse response) {
        response.setHeader("Connection", "keep-alive");
        response.setContentType("application/json");
        response.setHeader("Keep-Alive", "timeout=60");
        return new LoginCheckResponse(memberInfo.name());
    }
}
