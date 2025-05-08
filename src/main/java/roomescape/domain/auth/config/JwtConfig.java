package roomescape.domain.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.domain.auth.service.JwtManager;

@Configuration
public class JwtConfig {

    @Bean
    public JwtManager jwtManager(@Value("${auth.jwt.secret-key}") final String secretKey,
                                 @Value("${auth.jwt.expire-length}") final int validityInMilliseconds) {
        return new JwtManager(secretKey, validityInMilliseconds);
    }
}
