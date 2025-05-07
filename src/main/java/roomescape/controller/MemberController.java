package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.AuthService;
import roomescape.auth.CookieProvider;
import roomescape.controller.request.LoginMemberRequest;
import roomescape.controller.request.RegisterMemberRequest;
import roomescape.controller.response.CheckLoginUserResponse;
import roomescape.controller.response.LoginUserResponse;
import roomescape.controller.response.RegisterUserResponse;
import roomescape.exception.UnAuthorizedException;
import roomescape.service.MemberService;
import roomescape.service.param.LoginMemberParam;
import roomescape.service.param.RegisterMemberParam;
import roomescape.service.result.LoginMemberResult;
import roomescape.service.result.RegisterUserResult;

@Controller
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;
    private final CookieProvider cookieProvider;

    public MemberController(final MemberService memberService, final AuthService authService, final CookieProvider cookieProvider) {
        this.memberService = memberService;
        this.authService = authService;
        this.cookieProvider = cookieProvider;
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/members")
    public ResponseEntity<RegisterUserResponse> signup(@RequestBody final RegisterMemberRequest registerMemberRequest) {
        try {
            RegisterMemberParam registerMemberParam = registerMemberRequest.toServiceParam();
            RegisterUserResult registerUserResult = memberService.create(registerMemberParam);
            return ResponseEntity.ok(RegisterUserResponse.from(registerUserResult));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginMemberRequest loginMemberRequest, HttpServletResponse response) {
        try {
            LoginMemberParam loginMemberParam = loginMemberRequest.toServiceParam();
            LoginMemberResult loginMemberResult = memberService.login(loginMemberParam);

            String token = authService.createToken(loginMemberResult);
            response.addCookie(cookieProvider.create(token));

            return ResponseEntity.ok().body(LoginUserResponse.from(loginMemberResult));
        } catch (Exception e) { //TODO:ExceptionHandler로 처리하기
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckLoginUserResponse> loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new UnAuthorizedException();
        }
        Long id = authService.extractSubjectFromToken(cookies);
        return ResponseEntity.ok().body(CheckLoginUserResponse.from(memberService.findById(id)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        response.addCookie(cookieProvider.invalidate(cookies));
        return ResponseEntity.ok().build();
    }
}
