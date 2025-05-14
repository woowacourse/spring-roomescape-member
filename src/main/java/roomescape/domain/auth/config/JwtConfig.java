package roomescape.domain.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import roomescape.domain.auth.service.JwtManager;

@Configuration
public class JwtConfig {

    @Bean
    public JwtManager jwtManager(final JwtProperties jwtProperties) {
        return new JwtManager(jwtProperties.getSecretKey(), jwtProperties.getExpireLength());
    }
}
