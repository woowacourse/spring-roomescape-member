package roomescape.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import roomescape.jwt.JwtTokenProvider;

@TestConfiguration
public class TestConfig {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long expireLengthInMilliSeconds;

    @Bean
    @Primary
    public JwtTokenProvider testTokenProvider() {
        return new JwtTokenProvider(secretKey, expireLengthInMilliSeconds);
    }
}
