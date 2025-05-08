package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.exception.NotFoundException;
import roomescape.repository.MemberRepository;

@Service
public class LoginService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final MemberRepository memberRepository;

    public LoginService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Cookie doLogin(LoginRequest request) {
        Member member = getMember(request);
        System.out.println(member.getName());
        String accessToken = Jwts.builder()
                .setSubject(member.getName())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private Member getMember(LoginRequest request) {
        Optional<Member> member = memberRepository.findMember(request.email(), request.password());
        return member.orElseThrow(() -> new NotFoundException("[ERROR] 로그인에 실패했습니다."));
    }

    public String getNameFromCookie(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
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
