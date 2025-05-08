package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.request.MemberLoginRequest;
import roomescape.controller.dto.request.MemberSignUpRequest;
import roomescape.controller.dto.request.MemberSignUpResponse;
import roomescape.controller.dto.response.MemberLoginCheckResponse;
import roomescape.domain.LoginMember;
import roomescape.service.MemberService;
import roomescape.service.dto.request.MemberLoginCreation;
import roomescape.service.dto.request.MemberSignUpCreation;
import roomescape.service.dto.response.MemberSignUpResult;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public MemberSignUpResponse signup(@RequestBody @Valid MemberSignUpRequest request) {
        MemberSignUpCreation creation = MemberSignUpCreation.from(request);
        MemberSignUpResult signUpResult = memberService.register(creation);
        return MemberSignUpResponse.from(signUpResult);
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid final MemberLoginRequest request,
                      final HttpServletResponse servletResponse) {
        String loginToken = memberService.publishAccessToken(MemberLoginCreation.from(request));
        addCookieToken(servletResponse, loginToken);
    }

    private void addCookieToken(final HttpServletResponse servletResponse, final String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(1000 * 60 * 60 * 24);
        servletResponse.addCookie(cookie);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberLoginCheckResponse> checkLogin(LoginMember loginMember) {
        return ResponseEntity.ok(MemberLoginCheckResponse.from(loginMember));
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
