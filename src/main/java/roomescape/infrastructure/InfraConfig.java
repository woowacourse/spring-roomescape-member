package roomescape.infrastructure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class InfraConfig {

    @Bean
    public TokenManager tokenManager() {
        return new CookieTokenManager();
    }

    @Bean
    public TokenProvider tokenProvider(JwtProperties jwtProperties) {
        return new JwtTokenProvider(jwtProperties);
    }
}
