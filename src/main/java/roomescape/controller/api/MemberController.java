package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.annotation.AdminOnly;
import roomescape.controller.dto.request.MemberLoginRequest;
import roomescape.controller.dto.request.MemberSignUpRequest;
import roomescape.controller.dto.response.MemberLoginCheckResponse;
import roomescape.controller.dto.response.MemberResponse;
import roomescape.controller.dto.response.MemberSignUpResponse;
import roomescape.domain.LoginMember;
import roomescape.domain.MemberRoleType;
import roomescape.service.MemberService;
import roomescape.service.dto.request.MemberLoginCreation;
import roomescape.service.dto.request.MemberSignUpCreation;
import roomescape.service.dto.response.MemberResult;
import roomescape.service.dto.response.MemberSignUpResult;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @AdminOnly
    @GetMapping("/members")
    public List<MemberResponse> findAllMembers() {
        List<MemberResult> memberResults = memberService.getAllMemberByRole(MemberRoleType.MEMBER);
        return memberResults.stream()
                .map(MemberResponse::from)
                .toList();
    }

    @PostMapping("/members")
    public MemberSignUpResponse signup(@RequestBody @Valid MemberSignUpRequest request) {
        MemberSignUpCreation creation = MemberSignUpCreation.from(request);
        MemberSignUpResult signUpResult = memberService.register(creation);
        return MemberSignUpResponse.from(signUpResult);
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberLoginCheckResponse> checkLogin(LoginMember loginMember) {
        return ResponseEntity.ok(MemberLoginCheckResponse.from(loginMember));
    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid final MemberLoginRequest request,
                      final HttpServletResponse servletResponse) {
        String loginToken = memberService.publishAccessToken(MemberLoginCreation.from(request));
        int expireTime = (int) Duration.ofDays(1).toMillis();
        addCookieToken(servletResponse, loginToken, expireTime);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        addCookieToken(response, "", 0);
    }

    private void addCookieToken(final HttpServletResponse servletResponse, final String token, final int maxAge) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
        cookie.setMaxAge(maxAge);
        servletResponse.addCookie(cookie);
    }
}
