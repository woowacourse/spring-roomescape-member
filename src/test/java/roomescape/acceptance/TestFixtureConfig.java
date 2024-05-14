package roomescape.acceptance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import roomescape.acceptance.fixture.TokenFixture;

@Configuration
@PropertySource("classpath:application-test.properties")
public class TestFixtureConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public TokenFixture tokenFixture() {
        return new TokenFixture(secretKey);
    }
}
