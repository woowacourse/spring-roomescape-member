package roomescape.web.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.service.AuthorizationService;
import roomescape.service.request.LoginMember;
import roomescape.service.request.MemberLoginRequest;
import roomescape.service.response.MemberResponse;
import roomescape.service.response.Token;

@RestController
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody MemberLoginRequest request, HttpServletResponse response) {
        Token token = authorizationService.login(request);
        Cookie cookie = createCookieWithToken(token);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> getMemberInfo(LoginMember loginMember) {
        MemberResponse response = authorizationService.getLoginMemberInfo(loginMember);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = createCookieWithToken(new Token(null));
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    private Cookie createCookieWithToken(Token token) {
        Cookie cookie = new Cookie("token", token.value());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
