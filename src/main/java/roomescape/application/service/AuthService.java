package roomescape.application.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.domain.exception.UnauthorizedException;
import roomescape.domain.model.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.presentation.dto.request.LoginRequest;
import roomescape.presentation.dto.response.LoginResponse;

@Service
public class TokenLoginService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private final MemberRepository memberRepository;

    public TokenLoginService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public LoginResponse login(final LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email());
        if (!isPasswordCorrect(request.password(), member.getPassword())) {
            throw new UnauthorizedException();
        }

        String token = createToken(member);
        return new LoginResponse(token);
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

    private String createToken(final Member member) {
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
        return accessToken;
    }

    private boolean isPasswordCorrect(String getPassword, String savedPassword) {
        return true;
    }
}
