package roomescape.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.application.dto.LoginRequest;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Role;

@Service
public class AuthService {

    private final MemberService memberService;

    @Value("${jwt.secret.key}")
    private String secretKey;

    public AuthService(MemberService memberService) {
        this.memberService = memberService;
    }

    public String createTokenForAuthenticatedMember(LoginRequest request) {
        Member member = memberService.getMemberByEmail(request.email());
        validatePassword(request, member);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Role extractRoleFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        return Role.valueOf((String) Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().get("role"));
    }

    public Long extractMemberIdFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        return Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
    }

    public LoginMember getLoginMember(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);

        Claims body = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token).getBody();

        Long memberId = Long.valueOf(body.getSubject());
        String name = body.get("name", String.class);

        return new LoginMember(memberId, name);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void validatePassword(LoginRequest request, Member member) {
        if (member.notMatchesPassword(request.password())) {
            throw new IllegalArgumentException("로그인을 실패했습니다. 정보를 다시 확인해 주세요.");
        }
    }
}
