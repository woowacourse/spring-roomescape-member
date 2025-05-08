package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.config.LoginMember;
import roomescape.dto.AuthorizationResponse;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.entity.AccessToken;
import roomescape.service.MemberService;

@RestController
public class MemberController {

    //TODO : /signup, /logout API
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public List<MemberResponse> readMembers() {
        return memberService.findAllMembers();
    }

    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse createMember(
            @RequestBody MemberRequest request
    ) {
        return memberService.createMember(request);
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
    public AuthorizationResponse checkAuthorization(
            LoginMember member
    ) {
        return memberService.findMember(member);
    }
}
