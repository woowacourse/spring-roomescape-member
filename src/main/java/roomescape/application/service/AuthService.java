package roomescape.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.application.dto.LoginRequest;
import roomescape.domain.AuthMember;
import roomescape.domain.Member;
import roomescape.domain.Role;

@Service
public class AuthService {

    private static final String CLAIM_NAME = "name";
    private static final String CLAIM_ROLE = "role";
    private static final String TOKEN_NAME = "token";

    private final MemberService memberService;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.token.expiration-second}")
    private long expirationTime;

    public AuthService(MemberService memberService) {
        this.memberService = memberService;
    }

    public String createTokenForAuthenticatedMember(LoginRequest request) {
        Member member = memberService.getMemberByEmail(request.email());
        validatePassword(request, member);

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim(CLAIM_NAME, member.getName())
                .claim(CLAIM_ROLE, member.getRole())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime * 1000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public AuthMember extractAuthMemberFromRequest(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long memberId = Long.valueOf(claims.getSubject());
        String name = claims.get(CLAIM_NAME, String.class);
        Role role = Role.valueOf((String) claims.get(CLAIM_ROLE));

        return new AuthMember(memberId, name, role);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(TOKEN_NAME))
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
