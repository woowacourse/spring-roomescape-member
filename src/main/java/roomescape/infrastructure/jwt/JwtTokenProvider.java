package roomescape.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.exceptions.auth.AuthorizationException;
import roomescape.infrastructure.member.MemberInfo;

public class JwtTokenProvider {

    private static final String NAME_KEY = "name";
    private static final String EMAIL_KEY = "email";
    private static final String ROLE_KEY = "role";

    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(String secretKey, long validityInMilliseconds) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createToken(Member member) {
        Date validity = new Date(System.currentTimeMillis() + validityInMilliseconds);
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim(NAME_KEY, member.getName())
                .claim(EMAIL_KEY, member.getEmail())
                .claim(ROLE_KEY, member.getRole())
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public MemberInfo parseTokenToMemberInfo(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Long id = Long.parseLong(claims.getSubject());
            String name = (String) claims.get(NAME_KEY);
            String email = (String) claims.get(EMAIL_KEY);
            Role role = Role.valueOf((String) claims.get(ROLE_KEY));
            return new MemberInfo(id, name, email, role);
        } catch (ExpiredJwtException e) {
            throw new AuthorizationException("토큰이 만료되었습니다");
        } catch (SignatureException e) {
            throw new AuthorizationException("유효하지 않은 토큰입니다");
        } catch (RequiredTypeException | IllegalArgumentException e) {
            throw new AuthorizationException("토큰 파싱에 실패하였습니다");
        } catch (Exception e) {
            throw new AuthorizationException("토큰 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}

