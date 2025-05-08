package roomescape.application.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.domain.exception.UnauthorizedException;
import roomescape.domain.model.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.presentation.dto.request.TokenRequest;

@Service
public class TokenLoginService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private final MemberRepository memberRepository;

    public TokenLoginService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String login(final TokenRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password());
        return createToken(member);
    }

    private String createToken(final Member member) {
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("email", member.getEmail())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
        return accessToken;
    }

    public Member check(final String token) {
        Long memberId;
        try {
            memberId = Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody().getSubject());
        } catch (JwtException e) {
            throw new UnauthorizedException();
        }

        return memberRepository.findById(memberId);
    }
}
