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
import roomescape.config.LoginMember;
import roomescape.dto.response.MemberNameResponse;
import roomescape.dto.request.LoginRequest;
import roomescape.entity.AccessToken;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        memberService.validateMemberExistence(request);
        AccessToken token = memberService.createAccessToken(request);

        Cookie tokenCookie = new Cookie("token", token.getValue());
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");

        response.addCookie(tokenCookie);
        response.addHeader("Content-Type", "application/json");
    }

    @GetMapping("/login/check")
    @ResponseStatus(HttpStatus.OK)
    public MemberNameResponse checkAuthorization(
            LoginMember member
    ) {
        return memberService.findMember(member);
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
