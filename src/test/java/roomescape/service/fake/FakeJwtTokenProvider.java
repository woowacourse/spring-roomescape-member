package roomescape.service.fake;

import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import roomescape.model.User;
import roomescape.service.TokenProvider;

public class FakeJwtTokenProvider implements TokenProvider {

    private String secretKey = "secret";

    @Override
    public String createToken(User user) {
        Map<String, Object> claims = createClaimsByUser(user);
        return Jwts.builder()
                .setClaims(claims)
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
