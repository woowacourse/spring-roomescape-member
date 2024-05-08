package roomescape.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResonse;
import roomescape.service.MemberService;

@Controller
public class LoginController {

    private final MemberService memberService;

    public LoginController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/login";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginPage(@RequestBody MemberRequest memberRequest, HttpServletResponse response) {
        MemberResonse memberResponse = memberService.findBy(memberRequest);

        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";
        String accessToken = Jwts.builder()
                .setSubject(memberResponse.id().toString())
                .claim("name", memberResponse.name())
                .claim("role", "member")
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}
