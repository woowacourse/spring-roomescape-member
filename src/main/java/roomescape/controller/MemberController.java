package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.entity.AccessToken;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
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
        response.addIntHeader("Keep-Alive", 60);
    }
}
