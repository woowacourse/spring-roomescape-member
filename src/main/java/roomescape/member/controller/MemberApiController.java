package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.controller.request.LoginRequest;
import roomescape.member.controller.request.SignUpRequest;
import roomescape.member.controller.response.MemberNameResponse;
import roomescape.member.controller.response.MemberResponse;
import roomescape.member.resolver.Authenticated;
import roomescape.member.service.MemberService;

@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        String accessToken = memberService.login(request);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> check(@Authenticated Long memberId) {
        MemberNameResponse response = memberService.check(memberId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        MemberResponse response = memberService.signUp(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie expiredCookie = new Cookie("token", null);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        expiredCookie.setHttpOnly(true);
        response.addCookie(expiredCookie);

        return ResponseEntity.ok().build();
    }
}
