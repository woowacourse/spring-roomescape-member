package roomescape.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import roomescape.model.User;

@Component
public class JwtTokenProvider implements TokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @Override
    public String createToken(User user) {
        Map<String, Object> claims = createClaimsByUser(user);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public Claims getPayload(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Map<String, Object> createClaimsByUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId().toString());
        claims.put("role", user.getRole().toString());
        return claims;
    }
}
