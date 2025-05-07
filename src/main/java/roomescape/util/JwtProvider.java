package roomescape.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import roomescape.model.Customer;

@Component
public class JwtProvider {

    public String createToken(Customer customer){
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

        return Jwts.builder()
                .setSubject(customer.getEmail())
                .claim("name", customer.getName())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}

