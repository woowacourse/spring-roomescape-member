package roomescape.controller.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberNameResponse;
import roomescape.entity.AccessToken;
import roomescape.service.AuthorizationService;
import roomescape.web.LoginMember;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthorizationService authorizationService;

    public AuthController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        authorizationService.validateMemberExistence(request);
        AccessToken token = authorizationService.createAccessToken(request);

        Cookie tokenCookie = new Cookie("token", token.getValue());
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");

        response.addCookie(tokenCookie);
        response.addHeader("Content-Type", "application/json");
    }

    @GetMapping("/login/check")
    @ResponseStatus(HttpStatus.OK)
    public MemberNameResponse checkAuthorization(
            LoginMember loginMember
    ) {
        return new MemberNameResponse(loginMember.name());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(
            HttpServletResponse response
    ) {
        Cookie expiredCookie = new Cookie("token", null);
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        response.addCookie(expiredCookie);
    }
}
