package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.domain.member.Member;
import roomescape.dto.LoginCheckResponse;
import roomescape.dto.LoginRequest;
import roomescape.dto.LoginResponse;
import roomescape.dto.MemberResponse;
import roomescape.dto.SignUpRequest;
import roomescape.service.AuthenticationService;
import roomescape.service.MemberService;

@Controller
public class AuthenticationController {
    private final MemberService memberService;
    private final AuthenticationService authService;

    public AuthenticationController(MemberService memberService, AuthenticationService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "/login";
    }

    @PostMapping("/login")
    public HttpEntity<LoginResponse> postLogin(
            @RequestBody LoginRequest request,
            HttpServletResponse httpServletResponse
    ) {
        Cookie cookie = getCookie(authService.generateToken(request));
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(new LoginResponse("name"));
    }

    @GetMapping("/login/check")
    public HttpEntity<LoginCheckResponse> checkLogin(Member member) {
        return ResponseEntity.ok(new LoginCheckResponse(member.getName()));
    }

    @PostMapping("/signup")
    public HttpEntity<MemberResponse> postSignUp(@RequestBody SignUpRequest signUpRequest) {
        Long id = memberService.addUser(signUpRequest);
        Member member = memberService.findById(id);
        return ResponseEntity.created(URI.create("/members/" + id)).body(MemberResponse.from(member));
    }

    @GetMapping("/signup")
    public String postSignUp() {
        return "/signup";
    }

    @PostMapping("/logout")
    public HttpEntity<LoginCheckResponse> logout(HttpServletResponse response) {
        response.addCookie(getExpiredCookie(response));
        return ResponseEntity.ok().build();
    }

    private Cookie getCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private Cookie getExpiredCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        response.addCookie(cookie);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
