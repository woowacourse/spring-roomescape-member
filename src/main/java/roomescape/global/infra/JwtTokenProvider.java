package roomescape.global.infra;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("@{security.jwt.secret-key}")
  private String secretKey;
  @Value("${security.jwt.expire-length}")
  private long validityInMilliseconds;

  public String createToken(final String email, final String name) {
    final Claims claims = Jwts.claims().setSubject(email);
    claims.put("name", name);
    final Date now = new Date();
    final Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  public Map<String, String> getPayload(final String token) {
    final HashMap<String, String> payloads = new HashMap<>();
    final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

    payloads.put("name", (String) claims.get("name"));
    payloads.put("email", claims.getSubject());
    return payloads;
  }

  public boolean validateToken(final String token) {
    try {
      final Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

      return !claims.getBody().getExpiration().before(new Date());
    } catch (final JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}
