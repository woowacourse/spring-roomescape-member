package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import java.net.URI;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.domain.member.Member;
import roomescape.dto.LoginCheckResponse;
import roomescape.dto.LoginRequest;
import roomescape.dto.LoginResponse;
import roomescape.dto.SignUpRequest;
import roomescape.dto.SignUpResponse;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@Controller
public class FrontPageController {
    private final MemberService memberService;
    private final AuthService authService;

    public FrontPageController(final MemberService memberService, final AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @GetMapping("/reservation")
    public String reservation() {
        return "/reservation";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "/login";
    }

    @PostMapping("/login")
    public HttpEntity<LoginResponse> postLogin(@RequestBody LoginRequest request,
                                               HttpServletResponse httpServletResponse) {
        String token = authService.generateToken(request);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return ResponseEntity.ok(new LoginResponse("name"));
    }

    @GetMapping("/login/check")
    public HttpEntity<LoginCheckResponse> checkLogin(Member member) {
        return ResponseEntity.ok(new LoginCheckResponse(member.getName()));
    }

    @PostMapping("/signup")
    public HttpEntity<String> postSignUp(@RequestBody SignUpRequest signUpRequest) {
        Long id = memberService.addUser(signUpRequest);
        return ResponseEntity.created(URI.create("/users/" + id)).body("qwe");
    }

    @GetMapping("/signup")
    public String postSignUp() {
        return "/signup";
    }

    @PostMapping("/logout")
    public HttpEntity<LoginCheckResponse> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        response.addCookie(cookie);
        cookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        cookie.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/members")
//    public HttpEntity<SignUpResponse> postSignUp(@RequestBody SignUpRequest signUpRequest) {
//        Long id = userService.addUser(signUpRequest);
//        return ResponseEntity.created(URI.create("/users/" + id)).build();
//    }
}
