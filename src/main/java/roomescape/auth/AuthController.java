package roomescape.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.controller.request.LoginMemberRequest;
import roomescape.controller.response.CheckLoginUserResponse;
import roomescape.controller.response.LoginMemberResponse;
import roomescape.service.MemberService;
import roomescape.service.result.LoginMemberResult;

@Controller
public class AuthController {

    private final AuthService authService;
    private final CookieProvider cookieProvider;
    private final MemberService memberService;

    public AuthController(final AuthService authService, final CookieProvider cookieProvider, final MemberService memberService) {
        this.authService = authService;
        this.cookieProvider = cookieProvider;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginMemberResponse> login(@RequestBody LoginMemberRequest loginMemberRequest, HttpServletResponse response) {
        LoginMemberResult loginMemberResult = memberService.login(loginMemberRequest.toServiceParam());
        String token = authService.createToken(loginMemberResult);
        response.addCookie(cookieProvider.create(token));

        return ResponseEntity.ok().body(LoginMemberResponse.from(loginMemberResult));
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckLoginUserResponse> loginCheck(HttpServletRequest request) {
        String token = cookieProvider.extractToken(request.getCookies());
        Long id = authService.extractSubjectFromToken(token);
        return ResponseEntity.ok().body(CheckLoginUserResponse.from(memberService.findById(id)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        response.addCookie(cookieProvider.invalidate(request.getCookies()));
        return ResponseEntity.ok().build();
    }
}
