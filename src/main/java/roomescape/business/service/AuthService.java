package roomescape.business.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.business.domain.Member;
import roomescape.exception.UnauthorizedException;
import roomescape.persistence.dao.MemberDao;
import roomescape.presentation.dto.LoginCheckResponse;

@Service
public class AuthService {

    final static String secretKey = "ThisSecretKeyIsOnlyUseLearningTestSoIsNotImportant1234567890Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    final MemberDao memberDao;

    public AuthService(final MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public String login(final String email, final String password) {
        Member member = memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new UnauthorizedException("해당하는 사용자를 찾을 수 없습니다. email: %s".formatted(email)));

        final String accessToken = createAccessToken(member.getId(), member.getName(), member.getEmail());
        return accessToken;
    }

    private String createAccessToken(final Long id, final String name, final String email) {
        return Jwts.builder()
                .claim("id", id)
                .claim("name", name)
                .claim("email", email)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public LoginCheckResponse getMemberNameByAccessToken(final String accessToken) {
        try {
            final String name= Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseSignedClaims(accessToken).getPayload().get("name",String.class);
            return new LoginCheckResponse(name);
        } catch (JwtException e) {
            throw new UnauthorizedException("유효하지 않은 AccessToken 입니다.");
        }
    }
}
