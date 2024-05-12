package roomescape.infrastructure.authentication;

import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Password;
import roomescape.infrastructure.persistence.MemberRepository;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthenticatedMemberProfile;
import roomescape.service.auth.AuthenticationFailException;
import roomescape.service.auth.AuthenticationRequest;
import roomescape.service.auth.UnauthorizedException;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtAuthService implements AuthService {

    private static final String NAME_CLAIM_KEY = "name";
    private static final String IS_ADMIN_CLAIM_KEY = "isAdmin";

    private final MemberRepository memberRepository;
    private final JwtProperties jwtProperties;

    public JwtAuthService(MemberRepository memberRepository, JwtProperties jwtProperties) {
        this.memberRepository = memberRepository;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String authenticate(AuthenticationRequest request) {
        Member member = findMember(request);
        checkPassword(request, member);
        return generateToken(member);
    }

    private Member findMember(AuthenticationRequest request) {
        return memberRepository
                .findByEmail(request.email())
                .orElseThrow(AuthenticationFailException::new);
    }

    private void checkPassword(AuthenticationRequest request, Member member) {
        Password requestedPassword = new Password(request.password());

        if (!member.isValidPassword(requestedPassword)) {
            throw new AuthenticationFailException();
        }
    }

    private String generateToken(Member member) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes());

        return Jwts.builder()
                .subject(member.getId().toString())
                .claim(NAME_CLAIM_KEY, member.getName().value())
                .claim(IS_ADMIN_CLAIM_KEY, member.isAdmin())
                .expiration(new Date(getExpirationMillis()))
                .signWith(key)
                .compact();
    }

    private Long getExpirationMillis() {
        return System.currentTimeMillis() + (1000L * jwtProperties.tokenExpirationSecond());
    }

    @Override
    public AuthenticatedMemberProfile authorize(String token) {
        try {
            return parseToken(token);
        } catch (JwtException exception) {
            throw new UnauthorizedException();
        }
    }

    private AuthenticatedMemberProfile parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes());
        Claims payload = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long id = Long.valueOf(payload.getSubject());
        String name = payload.get(NAME_CLAIM_KEY, String.class);
        boolean isAdmin = payload.get(IS_ADMIN_CLAIM_KEY, Boolean.class);

        return new AuthenticatedMemberProfile(id, name, isAdmin);
    }
}
