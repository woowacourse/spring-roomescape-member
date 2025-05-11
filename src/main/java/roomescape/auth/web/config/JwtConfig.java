package roomescape.auth.web.config;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private int expiration;
    @Value("${jwt.issuer}")
    private String issuer;

    @Bean
    public JwtBuilder jwtBuilder() {
        return Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .issuer(issuer)
                .signWith(jwtKeys());
    }

    @Bean
    public JwtParser jwtParser() {
        return Jwts.parser()
                .verifyWith(jwtKeys())
                .build();
    }

    @Bean
    public SecretKey jwtKeys() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
