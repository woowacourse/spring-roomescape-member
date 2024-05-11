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
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthenticatedMemberProfile;
import roomescape.service.auth.AuthenticationFailException;
import roomescape.service.auth.AuthenticationRequest;
import roomescape.service.auth.UnauthorizedException;

@Component
public class JwtAuthService implements AuthService {

    private static final String NAME_CLAIM_KEY = "name";
    private static final String IS_ADMIN_CLAIM_KEY = "isAdmin";
    private static final String SECRET_KEY_VALUE = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY_VALUE.getBytes());

    private final MemberRepository memberRepository;

    public JwtAuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
        if (!member.isValidPassword(request.password())) {
            throw new AuthenticationFailException();
        }
    }

    private String generateToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim(NAME_CLAIM_KEY, member.getName().value())
                .claim(IS_ADMIN_CLAIM_KEY, member.isAdmin())
                .signWith(KEY)
                .compact();
    }

    @Override
    public AuthenticatedMemberProfile authorize(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(KEY)
                .build();
        try {
            Claims payload = parser.parseSignedClaims(token)
                    .getPayload();
            Long id = Long.valueOf(payload.getSubject());
            String name = payload.get(NAME_CLAIM_KEY, String.class);
            boolean isAdmin = payload.get(IS_ADMIN_CLAIM_KEY, Boolean.class);
            return new AuthenticatedMemberProfile(id, name, isAdmin);
        } catch (JwtException e) {
            throw new UnauthorizedException();
        }
    }
}
