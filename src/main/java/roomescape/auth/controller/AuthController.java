package roomescape.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.controller.dto.LoginRequest;
import roomescape.auth.controller.dto.response.CredentialResponse;
import roomescape.auth.service.AuthService;
import roomescape.auth.service.dto.response.CredentialServiceResponse;
import roomescape.global.util.CookieUtils;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String authenticationToken = authService.createAuthenticationToken(loginRequest.email(), loginRequest.password());
        response.addCookie(CookieUtils.createBasic("token", authenticationToken));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/login/check")
    public CredentialResponse loginCheck(HttpServletRequest request) {
        Cookie cookie = CookieUtils.extractFromCookiesByName(request.getCookies(), "token");
        CredentialServiceResponse response = authService.getCredentialDetails(cookie.getValue());
        return CredentialResponse.from(response);
    }
}
