package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
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

    @PostMapping("/login") //TODO: 응답 형식 고려
    public ResponseEntity<LoginMemberResponse> login(@RequestBody LoginMemberRequest loginMemberRequest, HttpServletResponse response) {
        LoginMemberResult loginMemberResult = memberService.login(loginMemberRequest.toServiceParam());
        String token = authService.createToken(loginMemberResult);

        response.setHeader(HttpHeaders.SET_COOKIE, cookieProvider.create(token).toString());
        return ResponseEntity.ok().body(LoginMemberResponse.from(loginMemberResult));
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckLoginUserResponse> loginCheck(@CookieValue("token") Cookie cookie) {
        String token = cookieProvider.extractTokenFromCookie(cookie);
        Long id = authService.extractSubjectFromToken(token);
        return ResponseEntity.ok().body(CheckLoginUserResponse.from(memberService.findById(id)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue("token") Cookie cookie, HttpServletResponse response) {
        response.setHeader(HttpHeaders.SET_COOKIE, cookieProvider.invalidate(cookie).toString());
        return ResponseEntity.ok().build();
    }
}
