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

    private static final String AUTHENTICATION_FAIL_MESSAGE = "올바른 인증 정보를 입력해주세요.";
    private static final String SECRET_KEY_VALUE = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY_VALUE.getBytes());

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String authenticate(AuthenticationRequest request) {
        Member member = findMember(request);
        checkPassword(request, member);
        return generateToken(member);
    }

    private Member findMember(AuthenticationRequest request) {
        return memberRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException(AUTHENTICATION_FAIL_MESSAGE));
    }

    private void checkPassword(AuthenticationRequest request, Member member) {
        if (!member.isValidPassword(request.password())) {
            throw new IllegalArgumentException(AUTHENTICATION_FAIL_MESSAGE);
        }
    }

    private String generateToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("name", member.getName().value())
                .claim("isAdmin", member.isAdmin())
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
            boolean isAdmin = payload.get("isAdmin", Boolean.class);
            return new AuthenticatedMemberProfile(id, name, isAdmin);
        } catch (JwtException e) {
            throw new UnauthorizedException();
        }
    }
}
