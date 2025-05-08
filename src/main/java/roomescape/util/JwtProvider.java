package roomescape.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.stereotype.Component;
import roomescape.model.Customer;

@Component
public class JwtProvider {
    private final long EXPIRATION_TIME = 60 * 60 * 1000;


    public String createToken(Customer customer){
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(customer.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("name", customer.getName())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}

