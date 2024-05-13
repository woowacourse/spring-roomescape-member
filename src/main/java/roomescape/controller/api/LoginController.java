package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.config.JwtTokenProvider;
import roomescape.dto.MemberCheckResponse;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.service.MemberService;

@Controller
public class LoginController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(final MemberService memberService, final JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginPage(@RequestBody MemberRequest memberRequest, HttpServletResponse response) {
        MemberResponse memberResponse = memberService.findBy(memberRequest);

        String accessToken = jwtTokenProvider.createToken(memberResponse.id(), memberResponse.name(), memberResponse.role());

        Cookie cookie = new Cookie(JwtTokenProvider.TOKEN_COOKIE_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberCheckResponse> loginCheckPage(HttpServletRequest request) {
        MemberCheckResponse memberCheckResponse = new MemberCheckResponse(jwtTokenProvider.extractMemberId(request), jwtTokenProvider.extractName(request));

        return ResponseEntity.ok().body(memberCheckResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutPage(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
