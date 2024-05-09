package roomescape.controller.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.AuthenticatedUser;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.AuthResponse;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

import java.util.Optional;

@Controller
public class LoginPageController { // TODO Page 가 맞을까??

    private final AuthService authService;
    private final MemberService memberService;

    public LoginPageController(AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Optional<MemberResponse> optionalLoginResponse = memberService.login(loginRequest);
        if (optionalLoginResponse.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        MemberResponse memberResponse = optionalLoginResponse.get();
        Cookie cookie = makeCookie(memberResponse);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private Cookie makeCookie(MemberResponse memberResponse) {
        TokenRequest tokenRequest = new TokenRequest(memberResponse.email(), memberResponse.name());
        TokenResponse tokenResponse = authService.createToken(tokenRequest);
        Cookie cookie = new Cookie("token", tokenResponse.token());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @GetMapping("/login/check")
    @ResponseBody
    public LoginCheckResponse loginCheck(@AuthenticatedUser AuthResponse authResponse) {
        Optional<MemberResponse> optionalMemberResponse = memberService.findMemberByEmail(authResponse.email());
        if (optionalMemberResponse.isEmpty()) {
            return null; // TODO 조회하는 회원이 없는 경우 무엇을 반환하는게 좋을까??
        }
        return new LoginCheckResponse(optionalMemberResponse.get().name());
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
