package roomescape.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.infrastructure.jwt.JwtTokenProvider;

@Configuration
public class AuthenticationConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${security.jwt.token.secret-key}") String secretKey,
            @Value("${security.jwt.token.expire-length}") Long validityInMilliseconds
    ) {
        return new JwtTokenProvider(secretKey, validityInMilliseconds);
    }
    
}
