package roomescape.infrastructure.authentication;

import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.infrastructure.persistence.MemberRepository;

@Component
public class AuthService {

    private static final String SECRET_KEY_VALUE = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY_VALUE.getBytes());

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String authenticate(AuthenticationRequest request) {
        Member member = memberRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("올바른 인증 정보를 입력해주세요."));

        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName().value())
                .signWith(KEY)
                .compact();
    }

    public AuthenticatedMemberProfile authorize(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(KEY)
                .build();
        try {
            Claims payload = parser.parseSignedClaims(token)
                    .getPayload();
            Long id = Long.valueOf(payload.getSubject());
            String name = payload.get("name", String.class);
            return new AuthenticatedMemberProfile(id, name);
        } catch (JwtException e) {
            throw new UnauthorizedException();
        }
    }
}
