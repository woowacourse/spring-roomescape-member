package roomescape;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;

@Component
public class JwtTokenProvider {

    public String createToken(Member member) {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        String accessToken = Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        return accessToken;
    }

    public Long findMemberIdByToken(String token) {
        Long id = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes())).build()
                .parseClaimsJws(token).getBody().getSubject());
        return id;
    }
}
