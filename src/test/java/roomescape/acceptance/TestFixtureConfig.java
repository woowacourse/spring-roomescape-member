package roomescape.acceptance;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import roomescape.acceptance.fixture.TokenFixture;

@Configuration
@PropertySource("classpath:application-test.properties") //todo: 이 클래스 지워도 동작하나?? 굳이 빈으로 등록할 필요 없나?
public class TestFixtureConfig {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Bean
    public TokenFixture tokenFixture() {
        return new TokenFixture(secretKey);
    }
}
