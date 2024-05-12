package roomescape.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.config.JwtTokenProvider;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberCheckResponse;
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

        String accessToken = jwtTokenProvider.createToken(memberResponse.id(), memberResponse.role());

        Cookie cookie = new Cookie(JwtTokenProvider.TOKEN_COOKIE_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberCheckResponse> loginCheckPage(HttpServletRequest request) {
        String token = extractTokenFromCookie(request.getCookies());

        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());

        MemberCheckResponse memberCheckResponse = memberService.findById(memberId);

        return ResponseEntity.ok().body(memberCheckResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutPage(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    private String extractTokenFromCookie(final Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
