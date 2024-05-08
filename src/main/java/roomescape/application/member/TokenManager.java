package roomescape.application.member;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import roomescape.application.member.dto.response.MemberResponse;
import roomescape.application.member.dto.response.TokenResponse;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@Component
public class TokenManager {

    @Value("${jwt.secret}")
    private String secret;

    private final MemberRepository memberRepository;

    public TokenManager(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public TokenResponse createToken(Member member) {
        String token = Jwts.builder()
                .claim("userId", member.getId())
                .signWith(getSecretKey())
                .compact();
        return new TokenResponse(token);
    }

    public MemberResponse parseToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰 값이 없습니다.");
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        Long id = claims.get("userId", Long.class);
        Member member = memberRepository.getById(id);
        return MemberResponse.from(member);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
