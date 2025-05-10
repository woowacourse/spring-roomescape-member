package roomescape.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import roomescape.member.controller.dto.LoginCheckResponse;
import roomescape.member.controller.dto.LoginRequest;
import roomescape.member.controller.dto.SignupRequest;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;
import roomescape.member.service.AuthService;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup";
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "token=" + token + "; Path=/; HttpOnly");
        headers.add("Keep-Alive", "timeout=60");

        return ResponseEntity.ok().headers(headers).build();
    }

    @ResponseBody
    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(HttpServletRequest request) {
        return ResponseEntity.ok(authService.checkLogin(request.getCookies()));
    }

    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<Member> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok(memberService.signup(signupRequest));
    }
}
