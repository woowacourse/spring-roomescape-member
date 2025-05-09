package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginMember;
import roomescape.dto.MemberLoginRequestDto;
import roomescape.dto.MemberResponse;
import roomescape.service.AuthenticationService;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/api")
public class MemberController {
    private final AuthenticationService authenticationService;
    private final MemberService memberService;

    public MemberController(final AuthenticationService authenticationService, final MemberService memberService) {
        this.authenticationService = authenticationService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody MemberLoginRequestDto memberLoginRequestDto,
                                          HttpServletResponse response) {
        String accessToken = memberService.loginMember(memberLoginRequestDto);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setHeader("Keep-Alive", "timeout=60");

        return ResponseEntity.ok().build();

    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> loginCheck(@CurrentMember LoginMember member) {
        return ResponseEntity.ok(new MemberResponse(member.name().getName()));
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }

}

